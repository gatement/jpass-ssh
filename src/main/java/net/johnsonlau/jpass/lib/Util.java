package net.johnsonlau.jpass.lib;

public class Util {
	public static void printBytes(byte[] input, int len) {
		for (int i = 0; i < len; i++) {
			if (i % 16 == 0) {
				System.out.printf("0x%04x: ", i);
			}
			System.out.printf("%02x", input[i]);
			if (i % 2 == 1) {
				System.out.printf(" ");
			}
			if ((i + 1) % 16 == 0 && (i + 1) < len) {
				System.out.printf("\n");
			}
		}
		System.out.println();
	}
}
