package rsa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class RSA {
    private static BigInteger[] PubKey = new BigInteger[2];
    private static BigInteger[] PriKey = new BigInteger[2];

    public static void getKeyPair() {
        BigInteger p = BigInteger.probablePrime(1024,new Random());
        BigInteger q = BigInteger.probablePrime(1024,new Random());
        BigInteger n = p.multiply(q);
        BigInteger Eun = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.probablePrime(128,new Random());
        BigInteger d = BigInteger.ONE;
        while (true) {
            BigInteger[] temp = ex_gcd(e,Eun);
            if(temp[0].compareTo(BigInteger.ONE)==0&&temp[1].compareTo(BigInteger.ZERO)==1){
                d = temp[1];
                break;
            }
            e = e.add(BigInteger.ONE);
        }
        PubKey[0]=e;
        PubKey[1]=n;
        PriKey[0]=d;
        PriKey[1]=n;
        System.out.println("p="+p.toString());
        System.out.println("q="+q.toString());
        System.out.println("$(n)="+Eun.toString());
        System.out.println("e="+PubKey[0].toString());
        System.out.println("n="+PubKey[1].toString());
        System.out.println("d="+PriKey[0].toString());
    }

    public static BigInteger[] ex_gcd(BigInteger a, BigInteger b, BigInteger[] x, BigInteger[] y){

        BigInteger gcd;
        BigInteger[] result = new BigInteger[3];

        if(b.compareTo(BigInteger.ZERO)==0){
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

    public static ArrayList<Integer> readFile(String path){
        StringBuffer ret = new StringBuffer();
        ArrayList<Integer> M = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                ret.append(line);
            }
            for(int j=0, i=0;i<ret.length();i+=2){
                if(i==ret.length()-1){
                    M.add(ret.charAt(i)*1000);
                    break;
                }
                M.add(ret.charAt(i)*1000+ret.charAt(i+1));
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
        return M;
    }

    public static void MtoString(ArrayList<Integer> R){
        StringBuffer temp = new StringBuffer();
        for(int i=0;i<R.size();i++){
            if(i==(R.size()-1)){
                if (R.get(i) % 1000 == 0) {
                    temp.append((char) (R.get(i)/1000));
                    break;
                }
            }
            temp.append((char)(R.get(i)/1000));
            temp.append((char)(R.get(i)%1000));
        }
        System.out.println(temp.toString());
    }

    public static ArrayList<BigInteger> encode(ArrayList<Integer> M,BigInteger[] Pubkey){
        ArrayList<BigInteger> C = new ArrayList<>();
        int i = 0;
        for(int m : M){
            C.add(quick_pow(BigInteger.valueOf(m),Pubkey[0],Pubkey[1]));
        }
        return C;
    }

    public static ArrayList<Integer> decode(ArrayList<BigInteger> C,BigInteger[] Prikey){
        ArrayList<Integer> M = new ArrayList<>();
        int i = 0;
        for(BigInteger c : C){
            M.add(quick_pow(c,Prikey[0],Prikey[1]).intValue());
        }
        return M;
    }

    public static BigInteger quick_pow(BigInteger m,BigInteger e,BigInteger n){
        BigInteger ans = BigInteger.ONE;
        while (e.compareTo(BigInteger.ZERO)!=0){
            if((e.and(BigInteger.ONE)).compareTo(BigInteger.ZERO)!=0){
                ans=(ans.multiply(m)).mod(n);
            }
            m=m.multiply(m).mod(n);
            e=e.shiftRight(1);
        }
        return ans;
    }


    public static void main(String[] args) {
        getKeyPair();
        ArrayList<Integer> M= readFile("lab2-Plaintext.txt");
        System.out.println("明文："+M.toString());
        ArrayList<BigInteger> C = encode(M,PubKey);
        System.out.println("密文:"+C.toString());
        ArrayList<Integer> R = decode(C,PriKey);
        System.out.println("解密:"+R.toString());
        MtoString(R);
        return;
    }

}