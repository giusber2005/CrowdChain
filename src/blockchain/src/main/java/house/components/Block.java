package main.java.house.components;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.time.LocalDateTime;
import main.java.house.utils.*;

class Transaction {
    String sender;
    String receiver;
    double amount;
    long timestamp;
    String signature;

    public Transaction(String sender, String receiver, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = new Date().getTime();
    }
}

class Block {
    private int index;
    private LocalDateTime timestamp;
    private String data;
    private String hash;
    private String previousHash;
    private int nonce;

    public Block(int index, Transaction data, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.data = gson.toJson(data);
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = index + timestamp.toString() + data + previousHash + nonce;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                input.getBytes(StandardCharsets.UTF_8));
            
            return bytesToHex(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public String getData() { return data; }
    public int getIndex() { return index; }
    public LocalDateTime getTimeStamp() { return timestamp; }
}