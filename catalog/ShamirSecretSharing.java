import java.util.*;
import java.util.regex.*;

public class ShamirSecretSharing {

   
    public static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    
    public static double basisPolynomial(int i, int xValue, List<int[]> points) {
        int xi = points.get(i)[0];
        double yi = points.get(i)[1];
        double numerator = 1.0, denominator = 1.0;
        int k = points.size();

        
        for (int j = 0; j < k; j++) {
            if (i != j) {
                int xj = points.get(j)[0];
                numerator *= (xValue - xj);
                denominator *= (xi - xj);
            }
        }
        return yi * (numerator / denominator);
    }

    
    public static double lagrangeInterpolationConstant(List<int[]> points) {
        int k = points.size();
        double constantTerm = 0;

        
        for (int i = 0; i < k; i++) {
            constantTerm += basisPolynomial(i, 0, points);
        }

        return constantTerm;
    }

    
    public static List<int[]> parseAndDecodeInput(String jsonString) {
        List<int[]> points = new ArrayList<>();
        Pattern pattern = Pattern
                .compile("\"(\\d+)\":\\s*\\{\\s*\"base\":\\s*\"(\\d+)\",\\s*\"value\":\\s*\"(\\w+)\"\\s*}");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1)); // x-value is the key in the JSON
            int base = Integer.parseInt(matcher.group(2)); // base in which y-value is encoded
            int y = decodeValue(matcher.group(3), base); // decode y-value
            points.add(new int[] { x, y });
        }

        return points;
    }

    public static void main(String[] args) {
        // Sample Test Case
        String jsonString = """
                                                        {
                    "keys": {
                        "n": 4,
                        "k": 3
                    },
                    "1": {
                        "base": "10",
                        "value": "4"
                    },
                    "2": {
                        "base": "2",
                        "value": "111"
                    },
                    "3": {
                        "base": "10",
                        "value": "12"
                    },
                    "6": {
                        "base": "4",
                        "value": "213"
                    }
                }



                                                                """;

       
        List<int[]> decodedPoints = parseAndDecodeInput(jsonString);

        
        double constantTerm = lagrangeInterpolationConstant(decodedPoints);

       
        System.out.println("The constant term 'c' is: " + Math.round(constantTerm));
    }
}


// Output: The constant term 'c' is: 3