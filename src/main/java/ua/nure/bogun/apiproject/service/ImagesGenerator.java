package main.java.ua.nure.bogun.apiproject.service;

import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.GitHubAvatar;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

public class ImagesGenerator {
    private static final Logger logger = Logger.getLogger(ImagesGenerator.class);


    private static String imgToBase64(BufferedImage img, String formatName){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            logger.error("Failed to generate img",ioe);
        }
        return null;
    }
    public static String generate(){
        Properties prop = new Properties();
        String base64 = null;
        try {
            prop.load(
                    Hashing.class.getClassLoader().getResourceAsStream("project.properties")
            );
            String alg = prop.getProperty("images.format");
            Avatar avatar = GitHubAvatar.newAvatarBuilder().size(1200,1200).build();
            long hash = new Random().nextLong();
            BufferedImage img = avatar.create(hash);
            base64 = "data:image/" + alg + ";base64, " + imgToBase64(img,alg);
        } catch (Exception ex) {
            logger.error("Failed to generate img",ex);
        }
        return base64;
    }
}
