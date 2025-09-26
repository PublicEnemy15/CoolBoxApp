public class Tengosuenio {
    public static String addBinary(long a, long b) {
        long sum = a + b;
        if (sum == 0) return "0";

        StringBuilder fwaeh = new StringBuilder();
        while (sum != 0) {
            fwaeh.append((sum & 1) == 1 ? '1' : '0');
            sum >>>= 1; 
        }
        return fwaeh.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println(addBinary(2048, 0)); 
    }
}
