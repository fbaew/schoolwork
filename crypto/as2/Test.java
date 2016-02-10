public class Test {
    public static void main(String[] args) {
        RSA_encrypt encryptor = new RSA_encrypt();
        encryptor.initializeKeys();
//        encryptor.initializeKeysToAssignment();
        encryptor.encrypt_file("test.txt", "code.txt");
        encryptor.decrypt_file("code.txt", "result.txt");
    }
}