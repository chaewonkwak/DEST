# DEST
Digital Envelope Simulation Tool
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
-k  secret.key                   Generates an AES256 key for symmetric encryption and saves as a file named "secret.key" in this case

[sender, receiver]
-kp public.key private.key       Generates an RSA1024 keypair and saves as two files, "public.key" and "private.key" in this case

[sender]
-sign msg pkey skey rPkey --out ename      Makes digital signature and envelope at once for the message and saves as a file named in this case
                                    *Our tool uses RSAwithSHA256 and AES algorithms for generating digital envelope.
[receiver]
-vrfy rpkey Pkey ename          Verify whether the received digital envelope is from the correct sender and hasn't been modified.

"""
'''
