############################################
# Least-privilege policy for `library` Terraform
# Every statement below maps to specific resources in main.tf —
# nothing granted here is "just in case."
############################################

data "aws_iam_policy_document" "library_terraform_permissions" {

  # ---------- VPC / NETWORKING ----------
  # Maps to: aws_vpc.main, aws_subnet.public/private, aws_internet_gateway.igw,
  # aws_route_table.public/private, aws_route_table_association (x2),
  # aws_nat_gateway.nat, aws_eip.eip
  statement {
    sid    = "NetworkingLifecycle"
    effect = "Allow"
    actions = [
      "ec2:CreateVpc",
      "ec2:DeleteVpc",
      "ec2:ModifyVpcAttribute",
      "ec2:CreateSubnet",
      "ec2:DeleteSubnet",
      "ec2:ModifySubnetAttribute",
      "ec2:CreateInternetGateway",
      "ec2:DeleteInternetGateway",
      "ec2:AttachInternetGateway",
      "ec2:DetachInternetGateway",
      "ec2:CreateRouteTable",
      "ec2:DeleteRouteTable",
      "ec2:CreateRoute",
      "ec2:DeleteRoute",
      "ec2:AssociateRouteTable",
      "ec2:DisassociateRouteTable",
      "ec2:CreateNatGateway",
      "ec2:DeleteNatGateway",
      "ec2:AllocateAddress",
      "ec2:ReleaseAddress",
      "ec2:AssociateAddress",
      "ec2:DisassociateAddress",
      "ec2:CreateTags",
      "ec2:DeleteTags"
    ]
    # EC2 networking actions don't support resource-level ARN scoping —
    # this is an AWS API limitation, not a choice to leave it open.
    resources = ["*"]
  }

  # ---------- SECURITY GROUPS ----------
  # Maps to: aws_security_group.nginx, aws_security_group.backend-sg
  statement {
    sid    = "SecurityGroupLifecycle"
    effect = "Allow"
    actions = [
      "ec2:CreateSecurityGroup",
      "ec2:DeleteSecurityGroup",
      "ec2:AuthorizeSecurityGroupIngress",
      "ec2:AuthorizeSecurityGroupEgress",
      "ec2:RevokeSecurityGroupIngress",
      "ec2:RevokeSecurityGroupEgress"
    ]
    resources = ["*"] # same limitation as above
  }

  # ---------- EC2 INSTANCES ----------
  # Maps to: aws_instance.backend, aws_instance.nginx
  statement {
    sid    = "InstanceLifecycle"
    effect = "Allow"
    actions = [
      "ec2:RunInstances",
      "ec2:TerminateInstances",
      "ec2:StartInstances",
      "ec2:StopInstances",
      "ec2:ModifyInstanceAttribute"
    ]
    resources = ["*"]
  }

  # ---------- READ-ONLY DESCRIBE ----------
  # Maps to: data.aws_ami.al2023 (AMI lookup) + Terraform's own state
  # refresh, which calls Describe* on every resource type above to check
  # for drift. This is unavoidable and applies broadly by nature of what
  # "describe" means — read-only, so low risk even at wildcard scope.
  statement {
    sid    = "ReadOnlyDescribe"
    effect = "Allow"
    actions = [
      "ec2:DescribeImages",
      "ec2:DescribeInstances",
      "ec2:DescribeVpcs",
      "ec2:DescribeVpcAttribute",
      "ec2:DescribeSubnets",
      "ec2:DescribeInternetGateways",
      "ec2:DescribeRouteTables",
      "ec2:DescribeNatGateways",
      "ec2:DescribeAddresses",
      "ec2:DescribeAddressesAttribute",
      "ec2:DescribeSecurityGroups",
      "ec2:DescribeSecurityGroupRules",
      "ec2:DescribeTags",
      "ec2:DescribeAvailabilityZones"
    ]
    resources = ["*"]
  }


  # ---------- REMOTE STATE (S3) ----------
  # Maps to: backend "s3" block — bucket "nibin-library-terraform-state"
  # Note: no DynamoDB statement needed. use_lockfile = true means you're
  # on S3's native conditional-write locking (newer Terraform feature),
  # not the older DynamoDB lock-table pattern.
  statement {
    sid    = "RemoteStateS3"
    effect = "Allow"
    actions = [
      "s3:GetObject",
      "s3:PutObject",
      "s3:DeleteObject",
      "s3:ListBucket"
    ]
    resources = [
      "arn:aws:s3:::nibin-library-terraform-state",
      "arn:aws:s3:::nibin-library-terraform-state/*"
    ]
  }


    # ---------- IAM MANAGEMENT ----------
      statement {
        sid    = "IAMManagement"
        effect = "Allow"
        actions = [
          # OIDC Data Source
          "iam:GetOpenIDConnectProvider",
          "iam:ListOpenIDConnectProviders",

          # SSM Role & Instance Profile Creation
          "iam:CreateRole",
          "iam:DeleteRole",
          "iam:GetRole",
          "iam:ListRolePolicies",
          "iam:ListAttachedRolePolicies",
          "iam:AttachRolePolicy",
          "iam:DetachRolePolicy",
          "iam:CreateInstanceProfile",
          "iam:DeleteInstanceProfile",
          "iam:GetInstanceProfile",
          "iam:AddRoleToInstanceProfile",
          "iam:RemoveRoleFromInstanceProfile",
          "iam:PassRole"
        ]
        resources = ["*"]
      }
  }

resource "aws_iam_policy" "library_terraform_permissions" {
  name   = "gha-terraform-library-policy"
  policy = data.aws_iam_policy_document.library_terraform_permissions.json
}

resource "aws_iam_role_policy_attachment" "library_permissions_attach" {
  role       = aws_iam_role.github_actions_terraform.name
  policy_arn = aws_iam_policy.library_terraform_permissions.arn
}
