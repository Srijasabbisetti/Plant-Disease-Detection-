import java.util.Scanner;

public class CipherProgram {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose cipher type:");
        System.out.println("1. Caesar Cipher");
        System.out.println("2. Substitution Cipher");
        System.out.println("3. Hill Cipher");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                handleCaesarCipher(scanner);
                break;
            case 2:
                handleSubstitutionCipher(scanner);
                break;
            case 3:
                handleHillCipher(scanner);
                break;
            default:
                System.out.println("Invalid choice");
        }
        scanner.close();
    }
    
    private static void handleCaesarCipher(Scanner scanner) {
        System.out.println("Enter text: ");
        String text = scanner.nextLine();
        System.out.println("Enter shift value: ");
        int shift = scanner.nextInt();
        
        String encrypted = caesarEncrypt(text, shift);
        String decrypted = caesarDecrypt(encrypted, shift);
        
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
    
    private static String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) (((c - base + shift) % 26) + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private static String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - shift);
    }
    
    private static void handleSubstitutionCipher(Scanner scanner) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String key = "QWERTYUIOPASDFGHJKLZXCVBNM";
        
        System.out.println("Enter text: ");
        String text = scanner.nextLine().toUpperCase();
        
        String encrypted = substitutionEncrypt(text, alphabet, key);
        String decrypted = substitutionDecrypt(encrypted, alphabet, key);
        
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
    
    private static String substitutionEncrypt(String text, String alphabet, String key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = alphabet.indexOf(Character.toUpperCase(c));
                result.append(key.charAt(index));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private static String substitutionDecrypt(String text, String alphabet, String key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = key.indexOf(Character.toUpperCase(c));
                result.append(alphabet.charAt(index));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private static void handleHillCipher(Scanner scanner) {
        System.out.println("Enter text (length must be even): ");
        String text = scanner.nextLine().toUpperCase();
        
        int[][] key = {{2, 1}, {3, 4}};
        int[][] inverseKey = {{4, -1}, {-3, 2}};
        
        String encrypted = hillEncrypt(text, key);
        String decrypted = hillDecrypt(encrypted, inverseKey);
        
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
    
    private static String hillEncrypt(String text, int[][] key) {
        StringBuilder result = new StringBuilder();
        
        if (text.length() % 2 != 0) {
            text += 'X';
        }
        
        for (int i = 0; i < text.length(); i += 2) {
            int p1 = text.charAt(i) - 'A';
            int p2 = text.charAt(i + 1) - 'A';
            
            int c1 = (key[0][0] * p1 + key[0][1] * p2) % 26;
            int c2 = (key[1][0] * p1 + key[1][1] * p2) % 26;
            
            result.append((char) (c1 + 'A'));
            result.append((char) (c2 + 'A'));
        }
        
        return result.toString();
    }
    
    private static String hillDecrypt(String text, int[][] inverseKey) {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < text.length(); i += 2) {
            int c1 = text.charAt(i) - 'A';
            int c2 = text.charAt(i + 1) - 'A';
            
            int p1 = (inverseKey[0][0] * c1 + inverseKey[0][1] * c2) % 26;
            int p2 = (inverseKey[1][0] * c1 + inverseKey[1][1] * c2) % 26;
            
            if (p1 < 0) p1 += 26;
            if (p2 < 0) p2 += 26;
            
            result.append((char) (p1 + 'A'));
            result.append((char) (p2 + 'A'));
        }
        
        return result.toString();
    }
}