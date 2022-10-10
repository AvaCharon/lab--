package rsa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private static BigInteger[] PubKey = new BigInteger[2];
    private static BigInteger[] PriKey = new BigInteger[2];

    public static void getKeyPair() {
        BigInteger p = BigInteger.probablePrime(128,new Random());
        BigInteger q = BigInteger.probablePrime(128,new Random());
        BigInteger n = p.multiply(q);
        BigInteger Eun = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.probablePrime(128,new Random());
        BigInteger d = BigInteger.ONE;
        while (true) {
            BigInteger[] temp = ex_gcd(e,Eun);
            if(temp[0].equals(BigInteger.ONE)){
                d = temp[1];
                break;
            }
            e = e.add(BigInteger.ONE);
        }
        PubKey[0]=e;
        PubKey[1]=n;
        PriKey[0]=d;
        PriKey[1]=n;
    }

    public static BigInteger[] ex_gcd(BigInteger a, BigInteger b, BigInteger[] x, BigInteger[] y){

        BigInteger gcd;
        BigInteger[] result = new BigInteger[3];

        if(b.equals(BigInteger.ZERO)){
            result[0] = a;
            result[1] = x[0];
            result[2] = y[0];
            return result;
        }
        BigInteger q=a.divide(b);
        BigInteger tx1 = x[0].subtract(q.multiply(x[1]));
        BigInteger ty1 = y[0].subtract(q.multiply(y[1]));
        BigInteger[] tx = {x[1],tx1};
        BigInteger[] ty = {y[1],ty1};
        return ex_gcd(b,a.mod(b),tx,ty);
    }

    public static BigInteger[] ex_gcd(BigInteger a, BigInteger b){
        BigInteger[] x = {BigInteger.ONE,BigInteger.ZERO};
        BigInteger[] y = {BigInteger.ZERO,BigInteger.ONE};
        return ex_gcd(a,b,x,y);
    }

    public static void testKeyPair(){
        BigInteger p = BigInteger.valueOf(17);
        BigInteger q = BigInteger.valueOf(11);
    }

    public static int[] readFile(String path){
        StringBuffer ret;
        int[] M;
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                ret.append(line);
            }
            for(int j=0, i=0;i<ret.length();i++){
                M[j]
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void encode(){

    }

    public void decode(){

    }

    public static void main(String[] args) {
        getKeyPair();
        StringBuffer message = readFile("lab2-Plaintext.txt");
        System.out.println(PubKey[0].toString());
        System.out.println(PubKey[1].toString());
        System.out.println(PriKey[0].toString());
        System.out.println(PriKey[1].toString());
    }

}