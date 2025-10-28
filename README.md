# End-to-End AWS CI/CD Pipeline for a Java Application

This project demonstrates a complete, industry-standard CI/CD pipeline on AWS, fully automated with Infrastructure as Code (IaC). It builds a simple Java command-line calculator application, runs its unit tests, and securely deploys it to an EC2 server.

The entire workflow is triggered automatically on a `git push` to the main branch.

## Architecture

This project builds the complete architecture shown in the diagram below, integrating source control, build, artifact management, deployment, and infrastructure provisioning.

![Project Architecture Diagram](https://github.com/Animesh-108/Devops-web-project/raw/master/architecture-complete.png)

### Services Used

* **Source:** **GitHub** for version control.
* **Pipeline:** **AWS CodePipeline** to orchestrate the entire build-to-deploy process.
* **Connection:** **AWS CodeConnections** (formerly CodeStar Connections) to securely connect GitHub to CodePipeline.
* **Build:** **AWS CodeBuild** to compile the Java code, run unit tests, and package the `.jar` file.
* **Artifacts:** **AWS CodeArtifact** to act as a private Maven repository for dependencies (like JUnit).
* **Deployment:** **AWS CodeDeploy** to manage the rolling deployment of the application onto the EC2 instance.
* **Infrastructure:** **AWS CloudFormation** to provision and manage the entire infrastructure (EC2 instance, IAM Roles, Security Group) as code.
* **Compute:** **Amazon EC2** to host the final deployed Java application.

---

## The Application

The application is a simple command-line calculator written in Java. It is built using **Apache Maven** and includes unit tests with **JUnit**.

* `src/main/java/com/example/calculator/Calculator.java`: Contains the core logic (add, subtract, etc.).
* `src/main/java/com/example/calculator/Main.java`: The command-line entry point that parses arguments (e.g., `100 + 50`).
* `src/test/java/com/example/calculator/CalculatorTest.java`: Unit tests to validate the calculator logic.

---

## Key Project Files

This pipeline is driven by "Configuration as Code," which makes it repeatable, auditable, and easy to modify.

* `infrastructure.yml`: **(CloudFormation)** The main Infrastructure as Code (IaC) template. It defines the EC2 instance, the IAM role, and the security group.
* `buildspec.yml`: **(CodeBuild)** The build instruction file. It tells CodeBuild how to log in to CodeArtifact, install dependencies using Maven, and package the final `.jar` artifact.
* `appspec.yml`: **(CodeDeploy)** The deployment instruction file. It tells the CodeDeploy agent on the EC2 server where to copy the new `.jar` file and what scripts to run to start and validate the application.
* `settings.xml`: **(Maven)** The configuration file that directs Maven to use the private AWS CodeArtifact repository instead of the public Maven Central repository.
* `scripts/`: A directory containing helper scripts for the deployment:
    * `start_app.sh`: Runs the Java application with a test calculation and saves the output to a log file.
    * `stop_app.sh`: Cleans up old log files before a new deployment.

---

## How to Deploy This Project

1.  **Prerequisites:**
    * An AWS Account.
    * A GitHub repository containing the project code.
    * An EC2 Key Pair created in the AWS console (for SSH access).

2.  **Create AWS CodeArtifact Repository:**
    * Go to the **CodeArtifact** service.
    * Create a new domain (e.g., `my-company-domain`).
    * Create a new repository (e.g., `calculator-repo`) and connect it to the `maven-central` public upstream repository.
    * Update `settings.xml` and `buildspec.yml` with your AWS Account ID, Region, and repository details.

3.  **Create CloudFormation Stack:**
    * Go to the **CloudFormation** service.
    * Create a new stack, uploading the `infrastructure.yml` template.
    * When prompted for parameters, enter the **name of your EC2 Key Pair**.
    * Wait for the stack status to become `CREATE_COMPLETE`.

4.  **Create the CodeDeploy Application:**
    * Go to the **CodeDeploy** service.
    * Create a new application named `calculator-app` (Compute Platform: `EC2/On-premises`).
    * Create a deployment group named `calculator-dg` for this application.
    * Configure it to deploy to instances tagged `App: Calculator` (which the CloudFormation stack creates automatically).

5.  **Create the CodePipeline:**
    * Go to the **CodePipeline** service.
    * Create a new pipeline named `calculator-pipeline`.
    * **Source Stage:** Connect to your GitHub repository and branch.
    * **Build Stage:** Connect to **AWS CodeBuild**. Let the wizard create a new build project. It will automatically detect the `buildspec.yml` file. (Remember to add `AWSCodeArtifactReadOnlyAccess` permissions to the build project's IAM role).
    * **Deploy Stage:** Connect to **AWS CodeDeploy**, selecting the `calculator-app` application and `calculator-dg` deployment group.

6.  **Push to GitHub:**
    * Commit all the configuration files (`infrastructure.yml`, `buildspec.yml`, `appspec.yml`, `settings.xml`) to your GitHub repository.
    * Pushing to the main branch will automatically trigger the pipeline.

## How to Verify

After the pipeline succeeds, you can verify the deployment:

1.  Go to the **EC2** console and find the instance created by CloudFormation.
2.  SSH into the instance using your key pair:
    ```bash
    ssh -i "your-key-pair.pem" ec2-user@<your-ec2-public-dns>
    ```
3.  Check the log file created by the deployment script:
    ```bash
    cat /opt/calculator/last_run.log
    ```
4.  You will see the output of the test calculation (e.g., `Result: 201.0`), proving the entire pipeline was successful.
