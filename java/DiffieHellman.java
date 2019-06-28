import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman {
    private static final BigInteger p = new BigInteger("109074813561941592945029492935978450034815512495317221177410110" +
            "69661501689227856390285324738488368177697121641690764329692246987526746776627399942657854372335961570459" +
            "70922338040698100507861033047312331823982435279475700199860971612732540528796554502867919746776983759391" +
            "47598714252131587871957751914881183087991942693995848708754096571641916746749932615622652967520917227700" +
            "13775912481475637828805588610833271741540149751348931251160157763188902959606980116141577212825275394688" +
            "16519319333337503114777192360412281721018955834377615480468479252748867320362385355596601795122806756217" +
            "71357981987063432156190781325515370395079527123265240489498386949217448165230380349888136621050864726366" +
            "83765141310311023368374889997757440467336518272393953535403484148728546397192946943234501868841898225445" +
            "40647226987292160693184734654941906936646576130260972193280317171696418971553954161446191759093719524951" +
            "11670557736207348131929604120128351615426904438925772770028968411946028348045230620413002491387998113590" +
            "80269838682059693181678196808509986496944169079527129049624049377757896989172073563552274550661838158476" +
            "69135530549755439819480321732925869069136146085326382334628745456398071603058051634209386708703306545903" +
            "19960852382451372962513665912822110096773545051995240424819826281383109737426165038001727791697532413484" +
            "65746813073370173808303536806232163369494713061916864382493056864133802310460964509535940893755402850372" +
            "92470929395114028305547452584962074309438151825437902976012891749355198678420603722034900311364893046495" +
            "76140433393868614003784803091629254327368453364003263763910077450237154247930247369838869289242094647894" +
            "77338003877827414177864847701901088678797789916332186286405339826193224661548830114522918902523364872360" +
            "86654396093853898628805813177559162076363154436494477507871294119841637867701722166609831201845484078070" +
            "51804133686980839845462558692120130818563888808269940868653604519264956919811035365994311180230063610650" +
            "98650239436618294364265630079172820508944293888417488853982907077430529736053592775157496197308237732158" +
            "94755121761467887865327707115573804264519206349215850195195364813387526811742474131549802130246506341207" +
            "02033579770678070540694527543880626597851620970679570257924407538049023174103086261496878330620786968786" +
            "81084236399719832090776247580804999882755913927872676271824428928096468742282631724356423685882601391619" +
            "62836121481966092745325488641054238839295138992979335446110090325230955276870524611359124918392740353154" +
            "294858383359");
    private static final BigInteger g = new BigInteger("2");
    private static String dhExplanation = "We are using Diffie-Hellman key exchange.\n" +
            "(https://security.stackexchange.com/questions/45963/diffie-hellman-key-exchange-in-plain-english)\n" +
            "To use it, you will need to exchange your public keys before you can encrypt or decrypt.\n" +
            "We will generate your public and private keys for you so that you can accomplish this interchange first\n" +
            "";
    private BigInteger privateKey;
    private BigInteger sharedKey;
    private String username;

    public DiffieHellman(String username) throws IOException {
        this.username = username;
        JOptionPane.showMessageDialog(null, dhExplanation);
        privateKey = makePrivateKey();
        BigInteger publicKey = g.modPow(privateKey, p);
        Utilities.writeMessage(username + " Public Key.txt", publicKey.toString().getBytes());
        JOptionPane.showMessageDialog(null, "Your new public key has been saved as publicKey.txt"
                + "\nShare freely.");
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
        int hasPrivateKey = JOptionPane.showConfirmDialog(null, "Do you have a private key?", "", JOptionPane.YES_NO_OPTION);
        if (hasPrivateKey > 0) {
            privateKey = new BigInteger(8192, new Random());
            JOptionPane.showMessageDialog(null, "Your new private key has been saved as privateKey.txt"
                    + "\nDon't let anyone see!!! ");
            Utilities.writeMessage(username + " Private Key.txt", privateKey.toString().getBytes());
            return privateKey;
        } else {
            File privateKeyFile = Utilities.getFile("Please open the txt file that holds your private key.");
            return readBigInteger(privateKeyFile);
        }
    }

    private BigInteger makeSharedKey() throws IOException {
        int hasPublicKey = JOptionPane.showConfirmDialog(null, "Do you have the other guy's public key?", "", JOptionPane.YES_NO_OPTION);
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
