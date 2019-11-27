import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman {
    private static final BigInteger p = new BigInteger(
            "1090748135619415929450294929359784500348155124953172211774101106966150168922785639028532473848836817769712164169076432969224698752674677662739994265785437233596157045970922338040698100507861033047312331823982435279475700199860971612732540528796554502867919746776983759391475987142521315878719577519148811830879919426939958487087540965716419167467499326156226529675209172277001377591248147563782880558861083327174154014975134893125116015776318890295960698011614157721282527539468816519319333337503114777192360412281721018955834377615480468479252748867320362385355596601795122806756217713579819870634321561907813255153703950795271232652404894983869492174481652303803498881366210508647263668376514131031102336837488999775744046733651827239395353540348414872854639719294694323450186884189822544540647226987292160693184734654941906936646576130260972193280317171696418971553954161446191759093719524951116705577362073481319296041201283516154269044389257727700289684119460283480452306204130024913879981135908026983868205969318167819680850998649694416907952712904962404937775789698917207356355227455066183815847669135530549755439819480321732925869069136146085326382334628745456398071603058051634209386708703306545903199608523824513729625136659128221100967735450519952404248198262813831097374261650380017277916975324134846574681307337017380830353680623216336949471306191686438249305686413380231046096450953594089375540285037292470929395114028305547452584962074309438151825437902976012891749355198678420603722034900311364893046495761404333938686140037848030916292543273684533640032637639100774502371542479302473698388692892420946478947733800387782741417786484770190108867879778991633218628640533982619322466154883011452291890252336487236086654396093853898628805813177559162076363154436494477507871294119841637867701722166609831201845484078070518041336869808398454625586921201308185638888082699408686536045192649569198110353659943111802300636106509865023943661829436426563007917282050894429388841748885398290707743052973605359277515749619730823773215894755121761467887865327707115573804264519206349215850195195364813387526811742474131549802130246506341207020335797706780705406945275438806265978516209706795702579244075380490231741030862614968783306207869687868108423639971983209077624758080499988275591392787267627182442892809646874228263172435642368588260139161962836121481966092745325488641054238839295138992979335446110090325230955276870524611359124918392740353154294858383359");
    private static final BigInteger g = new BigInteger("2");
    private static String dhExplanation = "We are using Diffie-Hellman key exchange.\n(https://security.stackexchange.com/questions/45963/diffie-hellman-key-exchange-in-plain-english)\nTo use it, you will need to exchange your public keys before you can encrypt or decrypt.\nWe will generate your public and private keys for you so that you can accomplish this interchange first\n";
    private BigInteger privateKey;
    private BigInteger sharedKey;
    private String username;

    public DiffieHellman(String username) throws IOException {
        this.username = username;
        JOptionPane.showMessageDialog(null, dhExplanation);
        privateKey = makePrivateKey();
        BigInteger publicKey = g.modPow(privateKey, p);
        Utilities.writeMessage(username + " Public Key.txt", publicKey.toString().getBytes());
        JOptionPane.showMessageDialog(null, "Your new public key has been saved as publicKey.txt" + "\nShare freely.");
        sharedKey = makeSharedKey();
        System.out.println(sharedKey.toString());
    }

    private static BigInteger readBigInteger(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        fis.close();
        return new BigInteger(new String(bytes));
    }

    private BigInteger makePrivateKey() throws IOException {
        int hasPrivateKey = JOptionPane.showConfirmDialog(null, "Do you have a private key?", "",
                JOptionPane.YES_NO_OPTION);
        if (hasPrivateKey > 0) {
            privateKey = new BigInteger(8192, new Random());
            JOptionPane.showMessageDialog(null,
                    "Your new private key has been saved as privateKey.txt" + "\nDon't let anyone see!!! ");
            Utilities.writeMessage(username + " Private Key.txt", privateKey.toString().getBytes());
            return privateKey;
        } else {
            File privateKeyFile = Utilities.getFile("Please open the txt file that holds your private key.");
            return readBigInteger(privateKeyFile);
        }
    }

    private BigInteger makeSharedKey() throws IOException {
        int hasPublicKey = JOptionPane.showConfirmDialog(null, "Do you have the other guy's public key?", "",
                JOptionPane.YES_NO_OPTION);
        if (hasPublicKey > 0) {
            JOptionPane.showMessageDialog(null, "Come back once you have it, bucko.");
            System.exit(0);
        }
        File publicKeyFile = Utilities.getFile("Please open the text file that holds the other guy's public key.");
        BigInteger otherPublicKey = readBigInteger(publicKeyFile);
        return otherPublicKey.modPow(privateKey, p);
    }

    public BigInteger getSharedKey() {
        return sharedKey;
    }

    public int[] arrayKey() {
        String sKey = sharedKey.toString();
        int remainder = 2466 - sKey.length();
        if (remainder == -1) {
            sKey = sKey.substring(1);
        }
        while (remainder > 0) {
            sKey = '0' + sKey;
            remainder--;
        }
        if (sKey.length() != 2466) {
            throw new Error("Key is " + sKey.length() + " long instead of 2466.");
        }
        int[] key = new int[411];
        String currentInt = "";
        int keyIndex = 0;
        for (int i = 0; i < sKey.length(); i++) {
            if (i % 6 == 0 && i != 0) {
                key[keyIndex] = Integer.parseInt(currentInt);
                currentInt = "";
                keyIndex++;
            }
            currentInt += sKey.charAt(i);
        }
        key[keyIndex] = Integer.parseInt(currentInt);
        return key;
    }

    public int[] reverseArrayKey() {
        int[] key = arrayKey();
        int[] reverse = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            int keyI = key[key.length - i - 1];
            keyI = keyI % 2 == 0 ? keyI + 1 : keyI - 1;
            reverse[i] = keyI;
        }
        return reverse;
    }
}
