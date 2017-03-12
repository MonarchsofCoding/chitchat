package com.moc.chitchat.crypto;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static sun.management.jmxremote.ConnectorBootstrap.initialize;

/**
 * Created by spiros on 12/03/2017.
 */
public class CryptoFunctionsTest {
    private KeyPairGenerator mockgenerator ;
    private KeyFactory mockkeyFactory;
    private KeyPair mockKeypair;
    private CryptoFunctions  mockCryptofunctions;

    @Before
    public void setUp() {
        mockCryptofunctions = Mockito.mock(CryptoFunctions.class);
    }


    /**
     * Constructor for CryptoFunction.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testConstructor() throws NoSuchAlgorithmException {
        mockgenerator = KeyPairGenerator.getInstance("RSA");
        mockkeyFactory = KeyFactory.getInstance("RSA");

        KeyPairGenerator keyPairGenerator = mockgenerator;
        KeyFactory keyFactory = mockkeyFactory;

        assertEquals(mockgenerator, keyPairGenerator);
        assertEquals(mockkeyFactory, keyFactory);
    }

    /**
     * Generate KeyPair of PublicKey and PrivateKey.
     * @throws Exception
     */
    @Test
    public void testKeyPairgenerator() throws Exception {


        mockCryptofunctions.initialize();
        mockgenerator = KeyPairGenerator.getInstance("RSA");
        mockgenerator.initialize(4096,new SecureRandom());
        mockkeyFactory = KeyFactory.getInstance("RSA");
        mockKeypair = this.mockgenerator.generateKeyPair();
        assertNotNull(mockKeypair);


    }









}
