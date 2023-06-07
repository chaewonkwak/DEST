package Project;

import java.util.Scanner;

public class dest {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("$ ");
		String answer = s.nextLine();
		while (!answer.equals("exit")) {
			switch(answer) {
				case "dest" :  
					System.out.println("Digital Envelope Simulation Tool  - verision 0.0.1");
					System.out.println();
					break;
				case "dest -h":
					System.out.println(
							"""
								    __    ____  ____ _______
								   |   \\ |  __|/ ___|__   __|
								   |  _ \\| |__( (___   | |
								   | | | |  __|\\___ \\  | |
								   | |_| | |__  ___) ) | |
							Welcome to |_____|____|(____/  |_| version 0.0.1.
													
							-= Digital Envelope Simulation Tool  - version 0.0.1 =-
							
							*All generated files are located in the same path as this simulation tool.
							
							[file name notations]
							:: skey stands for secretkey.
							:: Each Pkey and pkey stand for sender's publickey and privatekey.
							:: Each rPkey and rpkey stands for receiver's publickey and privatekey.
							:: msg stands for message.
							:: ename stands for digital envelope.
							
							
							*Run the introduced commands in following order.
							
							-h Prints this help
							
							[sender]
							-k  secret.key 					Generates an AES256 key for symmetric encryption and saves as a file named "secret.key" in this case
							
							[sender, receiver]
							-kp public.key private.key 			Generates a RSA1024 keypair and saves as two files, "public.key" and "private.key" in this case
							
							[sender]
							-sign msg pkey skey rPkey --out ename 		Makes digital signature and envelope at once for the message and saves as a file named in this case
													*Our tool uses RSAwithSHA256 and AES algorithms for generating digital envelope.
							[receiver]
							-vrfy rpkey Pkey ename 				Verify whether the received digital envelope is from the correct sender and hasn't been modified.
															
							"""		
					);
					break;
				default:
					String[] tokens = answer.split(" ");
					if (answer.contains("dest -k")) {
						if (answer.contains("dest -kp")) {
							if (tokens.length == 4) {
								if(sign_verify.generateKeyPair(tokens[2], tokens[3])) {
									System.out.println("A keyPair is successfully generated.");
								}
								else {
									System.out.println("Generating keyPair is failed.");
								}
							}
							else {
								System.out.println("Usage : dest -kp [filename1] [filename2]");
								System.out.println("For more details... try \"dest -h\" for help.");
							}
							
						} else {
							if (tokens.length == 3) {
								if(sign_verify.generateKey(tokens[2])) {
									System.out.println("A secretKey is successfully generated.");
								}
								else {
									System.out.println("Generating secretKey is failed.");
								}
							}
							else {
								System.out.println("Usage : dest -k [filename]");
								System.out.println("For more details... try \"dest -h\" for help.");
							}
						}
					} else if (answer.contains("dest -sign")) {
						if (tokens.length == 8 && answer.contains("--out")) {
							if (sign_verify.sign(tokens[2], tokens[3], tokens[4], tokens[5], tokens[7])) {
								System.out.println("Your digital envelope is successfully generated....!");
							}
							else {
								System.out.println("Generating digital envelope is failed.");
							}
						}
						else {
							System.out.println("Usage : dest -sign [msg] [pkey] [skey] [rPkey] --out [ename]");
							System.out.println("For more details... try \"dest -h\" for help.");
						}
						
					} else if (answer.contains("dest -vrfy")) {
						if (tokens.length == 5) {
							System.out.println("Verification result : " + sign_verify.vrfy(tokens[2], tokens[3], tokens[4]));
						}
						else {
							System.out.println("Usage : dest -vrfy [rpkey] [Pkey] [ename]");
							System.out.println("For more details... try \"dest -h\" for help.");
						}
					} else {
						System.out.println("===========Command Not Found==========");
						System.out.println("Check valid commands using \"dest -h\".");
					}
					System.out.println();
					break;
			}
		
			System.out.print("$ ");
			answer = s.nextLine();
		} 
		System.out.println("done. Thank you for using DEST!");
		s.close();

	}

}
