# Capstone

## Overview
Create a CI/CD solution for spring-petclinic Java project and deploy a containerized application to the Cloud.
Components:
- Infrastructure automation
- CI/CD solution
- Monitoring
- Architecture diagram

**Source code**: GitHub
**Cloud**: Azure
**Infrastructure automation tool**: Terraform
**Remote state storage for Terraform**: Azure Blob Storage
**Configuration management tool**: Ansible
**CI/CD automation tool**: Jenkins
**Build tool**: Maven or Gradle
**Artifacts**: Docker images
**Artifact storage**: Nexus or Azure Container Registry.
**Persistent database for application**: Azure Database for MySQL.
**Scripts**: Python and/or Bash

1. **Infrastructure automation**
Create an infrastructure automation pipeline that prepares the environment for application deployment using the Infrastructure as Code approach. Infrastructure configuration should be in a separate repository from the application source code.

Some preparation steps can be done manually or by running automation scripts locally:
- If you’re going to use Amazon S3, Azure Blob Storage, or Google Cloud Storage as a remote state storage for Terraform, it should be created in advance.
- If you’re going to use GitLab SaaS (GitLab.com), you need a runner for your first job. In the beginning, it can be installed on your local machine.
- If you’re going to use Jenkins, then create a virtual machine, install Jenkins and take care of agents.

Use Gitlab or Jenkins to run the infrastructure provisioning pipeline.
It should include the following jobs:
- configuration formatting,
- configuration validation and scanning (optional),
- plan,
- provisioning resources (manual job),
- destroying resources (manual job).

Use Terraform to create the following resources in the cloud:
- A virtual machine with all the needed network-related resources and a Load Balancer. This VM will be used to host the application.
- A persistent database for application.
- (Optional)  Additional virtual machine with all the needed network-related resources to run additional software like Gitlab runners, Nexus (if used).

Use Ansible to install additional software to the virtual machines including:
- Docker and docker-compose
- Jenkins with workers or Gitlab runners
- Nexus (if used)

2. **CI/CD solution**
A Continuous Integration and Continuous Delivery solution for the Java application [spring-petclinic](https://www.google.com/url?q=https://github.com/spring-projects/spring-petclinic&sa=D&source=editors&ust=1742826146890651&usg=AOvVaw12nSpDnIHJoFRh9Q9pgBlX).
The repository with the spring-petclinic application source code should additionally have configuration files for Maven or Gradle and a Dockerfile.
In the artifact storage of your choice prepare a registry.

The pipeline for a merge request(MR) or a pull request(PR) should include:
1. static code analysis,
2. tests,
3. build,
4. creating an artifact (it can be tagged with a short commit hash),
5. pushing the artifact to the artifact storage

The pipeline for the main branch should include:
1. creating a Git tag in the repository using [Semantic Versioning](https://www.google.com/url?q=https://semver.org/&sa=D&source=editors&ust=1742826146891380&usg=AOvVaw2Ehwp_RwkeL8rod96UNqGt) approach (a minor version increases on each commit). A python script with [semver · PyPI](https://www.google.com/url?q=https://pypi.org/project/semver/&sa=D&source=editors&ust=1742826146891487&usg=AOvVaw3GMQIq4u9-QdhcbHro8c8j) can be used here.
2. creating an artifact with Git tag representing the version
3. pushing the artifact to the artifact storage
4. a manual deployment job that:
	1. connects to a virtual machine,
	2. checks if a previous version of the application is present and removes it,
	3. gets the image from the artifact storage,
	4. runs the application making sure it is connected to a MySQL database in the cloud,
	5. prints the link to the application.

5. Monitoring (optional)

Use a Cloud-based monitoring solution to create a dashboard with resource consumption metrics for the VM, where the spring-petclinic application is running.

4. Architecture diagram

Create an architecture diagram for your solution. The following resources can be used as a reference:

- [AWS Reference Architecture Diagrams](https://www.google.com/url?q=https://aws.amazon.com/architecture/reference-architecture-diagrams&sa=D&source=editors&ust=1742826146892219&usg=AOvVaw2LQV2-i476TWttpVdDwE2a)
- [GCP Cloud Reference Architectures and Diagrams](https://www.google.com/url?q=https://cloud.google.com/architecture&sa=D&source=editors&ust=1742826146892342&usg=AOvVaw1lcuX1HC8VrQBkK9vNMzkM)
- [Azure Architectures](https://www.google.com/url?q=https://learn.microsoft.com/en-us/azure/architecture/browse/&sa=D&source=editors&ust=1742826146892465&usg=AOvVaw2hDKhlurNq9Ljb9bgVr2aE)


## Preparation

	- Clean Ansible container:
	```bash
	docker run -it --name AnsibleProject ubuntu:latest
	# -------
	apt update
	apt install -y ansible pip
	ansible --version

# Uninstall arg-parse with apt and install with pip
# apt remove python3-argparse (or something similiar)
	ansible-galaxy collection install azure.azcollection --force
	pip3 install -r ~/.ansible/collections/ansible_collections/azure/azcollection/requirements.txt --break-system-packages

	# https://stackoverflow.com/questions/75608323/how-do-i-solve-error-externally-managed-environment-every-time-i-use-pip-3
	```
	- Export variables
	```bash
	export AZURE_SUBSCRIPTION_ID=<subscription_id>
	export AZURE_CLIENT_ID=<service_principal_app_id>
	export AZURE_SECRET=<service_principal_password>
	export AZURE_TENANT=<service_principal_tenant_id>
    export GITHUB_TOKEN=<github_token_id>
	```
    You can export it with azure_vars file by running: `source azure_vars`.

	- Add instance
	```bash
	ssh-keygen -f jenkins-master
	mv jenkins-master ~/.ssh/ # copy private key to .ssh
	# copy paste into ansible playbook public key
	```
    - Important: **copy paste into ansible playbook public key**
## Run

```
ansible-playbook create_instance.yml -e resourceGroup=<resourceGroupId>
```
