variable "aws_region" {
  default = "ap-south-1"
}


variable "instance_type"{
    default = "t3.micro"
}

variable "key_name"{
    description = "Existing EC2 key pair name for ssh"
    type = string
}

variable "app_port"{
    default = 8090
}

variable "my_ip" {
  description = "Your IP for SSH access, e.g. 1.2.3.4/32"
  type        = string
}