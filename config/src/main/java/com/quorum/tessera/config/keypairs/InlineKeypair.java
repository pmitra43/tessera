package com.quorum.tessera.config.keypairs;

import com.quorum.tessera.config.KeyDataConfig;
import com.quorum.tessera.config.PrivateKeyData;
import com.quorum.tessera.config.constraints.ValidBase64;
import com.quorum.tessera.config.keys.KeyEncryptorFactory;
import com.quorum.tessera.nacl.NaclException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;

import static com.quorum.tessera.config.PrivateKeyType.UNLOCKED;
import javax.validation.constraints.Size;

public class InlineKeypair implements ConfigKeyPair {

    @XmlElement
    private final String publicKey;

    @NotNull
    @XmlElement(name = "config")
    private final KeyDataConfig privateKeyConfig;

    private String password;

    private String cachedValue;

    public InlineKeypair(final String publicKey, final KeyDataConfig privateKeyConfig) {
        this.publicKey = publicKey;
        this.privateKeyConfig = privateKeyConfig;
    }

    public KeyDataConfig getPrivateKeyConfig() {
        return this.privateKeyConfig;
    }

    @Override
    @Size(min = 1)
    @NotNull
    @ValidBase64(message = "Invalid Base64 key provided")
    public String getPublicKey() {
        return this.publicKey;
    }

    @Override
    @NotNull
    @Size(min = 1)
    @ValidBase64(message = "Invalid Base64 key provided")
    @Pattern(regexp = "^((?!NACL_FAILURE).)*$", message = "Could not decrypt the private key with the provided password, please double check the passwords provided")
    @Pattern(regexp = "^((?!MISSING_PASSWORD).)*$", message = "{InlineKeyData.missingPassword.message}")
    public String getPrivateKey() {
        final PrivateKeyData pkd = privateKeyConfig.getPrivateKeyData();

        if (privateKeyConfig.getType() == UNLOCKED) {
            return privateKeyConfig.getValue();
        }

        if (this.cachedValue == null) {
            if (password == null) {
                this.cachedValue = "MISSING_PASSWORD";
            } else {
                try {
                    this.cachedValue = KeyEncryptorFactory.create().decryptPrivateKey(pkd, password).encodeToBase64();
                } catch (final NaclException ex) {
                    this.cachedValue = "NACL_FAILURE";
                }
            }
        }

        return this.cachedValue;
    }

    @Override
    public void withPassword(final String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
