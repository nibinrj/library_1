############################################
# OIDC PROVIDER — trust anchor for GitHub Actions
# One per AWS account, per issuer URL. If you've ever created this
# provider before (console or another project's Terraform), do NOT
# apply this block again — it will fail with "already exists."
# In that case, use `terraform import` instead (see note at bottom).
############################################

resource "aws_iam_openid_connect_provider" "github_actions" {
  url = "https://token.actions.githubusercontent.com"

  client_id_list = [
    "sts.amazonaws.com"
  ]

  thumbprint_list = [
    "6938fd4d98bab03faadb97b34396831e3780aea1"
  ]
}

############################################
# TRUST POLICY — who is allowed to assume the role
# Scoped to your repo + main branch only.
############################################

data "aws_iam_policy_document" "github_actions_trust" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRoleWithWebIdentity"]

    principals {
      type        = "Federated"
      identifiers = [aws_iam_openid_connect_provider.github_actions.arn]
    }

    condition {
      test     = "StringEquals"
      variable = "token.actions.githubusercontent.com:aud"
      values   = ["sts.amazonaws.com"]
    }

    condition {
      test     = "StringLike"
      variable = "token.actions.githubusercontent.com:sub"
      values = [
        "repo:${var.github_org}/${var.github_repo}:ref:refs/heads/main"
      ]
    }
  }
}

############################################
# ROLE — GitHub Actions assumes this via OIDC.
# Permissions attached separately in library-policy.tf
# (aws_iam_role_policy_attachment.library_permissions_attach).
############################################

resource "aws_iam_role" "github_actions_terraform" {
  name               = "gha-terraform-${var.github_repo}"
  assume_role_policy = data.aws_iam_policy_document.github_actions_trust.json
}

############################################
# OUTPUT — paste this ARN into your GitHub Actions workflow YAML
# under role-to-assume
############################################

output "github_actions_role_arn" {
  value = aws_iam_role.github_actions_terraform.arn
}

############################################
# VARIABLES needed by this file — add these to variables.tf if not
# already declared there, and set values in terraform.tfvars
############################################

variable "github_org" {
  description = "GitHub org or username"
  type        = string
  default     = "nibinrj"
}

variable "github_repo" {
  description = "GitHub repo name"
  type        = string
  default     = "library"
}
