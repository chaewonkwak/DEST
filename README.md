<div align="center">

   <h3>DEST</h3>

  Digital Envelope Simulation Tool
  <br>
</div>

## DEST Overview

This is a project aimed at understanding public key infrastructures which are underlying techniques of certificate systems.

DEST is a **Digital Envelope Simulation Tool** developed in Java, appropriately integrating asymmetric and symmetric keys for electronic envelope simulation.

DEST is implemented in command line style so it does not include a graphical user interface (GUI).

DEST applied rule #2, #7, #12, #22, #24, #26, #33, #38, #39, #41, #43, #49, #50, #54, #56, #63, #64 from [Java Coding Guidelines: 75 Recommendations for Reliable and Secure Programs](https://s3-ap-southeast-1.amazonaws.com/tv-prod/documents/null-Addison.Wesley.Java.Coding.Guidelines.Sep.2013.ISBN.032193315X.pdf).


## Files

This distribution of DEST consists of the following files:

  * [README.md](https://github.com/chaewonkwak/DEST/blob/main/README.md), a file you are currently reading.

  * [Code Review 1](https://github.com/chaewonkwak/DEST/blob/main/CodeReviewReport(1).pdf) and [Code Review 2](https://github.com/chaewonkwak/DEST/blob/main/CodeReviewReport(2).pdf), files with detailed description of how I applied secure coding rules.

  * [bin](https://github.com/chaewonkwak/DEST/tree/main/bin) and [src](https://github.com/chaewonkwak/DEST/tree/main/src) folders contain the actual program.


## Install and Run (Docker Hub)

1. Download the docker image of DEST from my docker repository.
```
docker pull chaecker/dest:latest
```

2. Check if the image is downloaded successfully.
```
docker images | grep dest
```

3. Run a docker container with DEST base image (e.g. CONTAINER_NAME = dest-container).

```
docker run -i -t --name [CONTAINER_NAME] chaecker/dest:latest
```

4. Put your input.
```
dest -h
```

5. If you want to check files in the docker container or put your secret/public/private key files or message file,

5-1. Exit from dest program first.
```
exit
```

5-2. Check if the container is marked as "Exited".
```
docker ps -a | grep dest
```

5-3. Restart the docker container **with -it options** and **bash command**.
```
docker start -it [CONTAINER_NAME] bash
```

5-4. Now you can access the docker container's local file system. 

**Every key and plaintext file should locate in /app/(working directory).**

When you generate keys using **dest CLI**, you can check the generated file with linux 'ls' command.
```
ls
```

5-4-1. If you want to use 'vi' command for adding your file, please install vim in the container.
```
apt-get update
apt-get install vim
```

5-4-2. if you want to use 'wget' for downloading your file, please install wgent first.
```
apt-get update
apt-get install wget
```

## Install and Run (Git Hub)

1. Download DEST codes from my github repository.
```
git clone https://github.com/chaewonkwak/DEST.git

ls

cd DEST
```

2. Build docker image (e.g. IMAGE_NAME = dest, TAG_NAME = latest). **Don't miss the dot at the last!**

```
docker build -t [IMAGE_NAME]:[TAG_NAME] .
```

3. Check if the image is created successfully.
```
docker images | grep [IMAGE_NAME]
```

4. Run a container with the created image.
```
docker run -i -t --name [CONTAINER_NAME] [IMAGE_NAME]:[TAG_NAME]
```

5. Put your input.
```
dest -h
```


## Install and Compile (Eclipse IDE)

**Java version requirement: above 15** 

1. Use Eclipse IDE and create a new Java project.

2. Take every files in [bin](https://github.com/chaewonkwak/DEST/tree/main/bin) in your local bin folder.

3. Take every files in [src](https://github.com/chaewonkwak/DEST/tree/main/src) in your local src folder.

4. Compile and run.


## Usage

You can use **dest** command to use DEST program.

Your input:
```
dest
```

The output:
```
Digital Envelope Simulation Tool  - verision 0.0.1
```

There are several options. If the option is **k**, you can write your command like below:
```
dest -k
```

## Help

**dest** command with **h** option shows you the following instruction.

Your input:
```
dest -h
```

The output:

```java

            __    ____  ____ _______
           |   \ |  __|/ ___|__   __|
           |  _ \| |__( (___   | |
           | | | |  __|\___ \  | |
           | |_| | |__  ___) ) | |
Welcome to |_____|____|(____/  |_| version 0.0.1.

-= Digital Envelope Simulation Tool  - version 0.0.1 =-

*All generated files are located in the same path as this README.md.

[file name notations]
:: skey stands for secretkey.
:: Each Pkey and pkey stand for sender's publickey and privatekey.
:: Each rPkey and rpkey stands for receiver's publickey and privatekey.
:: msg stands for message.
:: ename stands for digital envelope.


*Run the introduced commands in the following order.

-h Prints this help

[sender]
-k  secret.key                              Generates an AES256 key for symmetric encryption and saves as a file named "secret.key" in this case

[sender, receiver]
-kp public.key private.key                  Generates an RSA1024 keypair and saves as two files, "public.key" and "private.key" in this case

[sender]
-sign msg pkey skey rPkey --out ename       Makes digital signature and envelope at once for the message and saves as a file named in this case
                                            *Our tool uses RSAwithSHA256 and AES algorithms for generating digital envelope.
[receiver]
-vrfy rpkey Pkey ename                      Verify whether the received digital envelope is from the correct sender and hasn't been modified.

