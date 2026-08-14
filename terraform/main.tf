terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

    backend "s3" {
    bucket       = "nibin-library-terraform-state"
    key          = "library/terraform.tfstate"
    region       = "ap-south-1"
    encrypt      = true
    use_lockfile = true
  }
}

provider "aws"{
    region = var.aws_region
    
}


#--------------IAM ROLE FOR GITHUB ACTIONS----------------


resource "aws_iam_role" "ssm" {
  name = "library-ssm-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "ec2.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ssm" {
  role       = aws_iam_role.ssm.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "ssm" {
  name = "library-ssm-profile"
  role = aws_iam_role.ssm.name
}

# ---------- NETWORKING ----------


resource "aws_eip" "eip" {
  domain = "vpc"
  tags   = { Name = "nat-eip" }
}


resource "aws_nat_gateway" "nat" {
  allocation_id = aws_eip.eip.id
  subnet_id = aws_subnet.public.id
  depends_on = [aws_internet_gateway.igw]
}

resource "aws_vpc" "main" {
    cidr_block = "10.0.0.0/16"
    enable_dns_support = true
    enable_dns_hostnames = true
    tags = {Name = "Library-vpc"}
}


resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id
  tags = {Name = "librar-igw"}
}


resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id
  cidr_block = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone = "${var.aws_region}a"
  tags = {Name = "library-public-subnet"}
}

resource "aws_subnet" "private" {
  vpc_id     = aws_vpc.main.id
  cidr_block = "10.0.2.0/24"

  availability_zone = "${var.aws_region}a"
  tags = {Name = "library-private-subnet"}


}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route  {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = { Name = "library-public-rt" }
}


resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id

  route  {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_nat_gateway.nat.id
  }

  tags = { Name = "library-private-rt" }
}

resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public.id 
}


resource "aws_route_table_association" "private" {
  subnet_id      = aws_subnet.private.id
  route_table_id = aws_route_table.private.id
}


# ---------- SECURITY GROUP ----------
resource "aws_security_group" "nginx" {
  name    = "nginx-sg"
  description = "secuirty group for nginx"
  vpc_id = aws_vpc.main.id

  ingress {
    description = "SSH from my IP"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_ip]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "backend-sg" {
  name        = "backend-sg"
  description = "Allow SSH, app port, and outbound"
  vpc_id      = aws_vpc.main.id

  ingress{
    description = "SSH"
    from_port = 22
    to_port = 22
    protocol = "tcp"
    security_groups = [aws_security_group.nginx.id]
  }  


  ingress{
    description = "App port"
    from_port = var.app_port
    to_port = var.app_port
    protocol = "tcp"
    security_groups = [aws_security_group.nginx.id]

  }

  egress {
  from_port   = 0
  to_port     = 0
  protocol    = "-1"
  cidr_blocks = ["0.0.0.0/0"]
}

  tags = {Name = "library-sg"}
}


# ---------- AMI (latest Amazon Linux 2023) ----------

data "aws_ami" "al2023" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }
}

# ------------------- RESOURCES ----------------------
resource "aws_instance" "backend" {
  ami                       = data.aws_ami.al2023.id
  instance_type             = var.instance_type
  subnet_id                 = aws_subnet.private.id
  vpc_security_group_ids    = [aws_security_group.backend-sg.id]
  key_name                  = var.key_name  
  private_ip                = "10.0.2.10"

    user_data = <<-EOF
    #!/bin/bash

    dnf install -y docker

    systemctl enable docker
    systemctl start docker

    docker pull nibinrj/library-app:latest

    docker run -d \
        --restart unless-stopped \
        -p 8090:8090 \
        nibinrj/library-app:latest 
    EOF

  tags = { Name = "library-instance" }
}

resource "aws_instance" "nginx" {
  ami           = data.aws_ami.al2023.id
  instance_type = "t3.micro"

  vpc_security_group_ids = [aws_security_group.nginx.id]
  subnet_id              = aws_subnet.public.id

  user_data = <<-EOF
    #!/bin/bash

    dnf install -y nginx

    cat > /etc/nginx/conf.d/backend.conf <<'NGINX'
    server {
        listen 80;
        server_name _;

        location / {
            proxy_pass http://${aws_instance.backend.private_ip}:8090;

            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
    NGINX

    systemctl enable nginx
    systemctl start nginx
  EOF

  tags = {
    Name = "nginx-serverr"
  }
}


# ---------- OUTPUTS ----------
output "instance_public_ip" {
  value = aws_instance.nginx.public_ip
}

output "instance_id" {
  value = aws_instance.nginx.id
}