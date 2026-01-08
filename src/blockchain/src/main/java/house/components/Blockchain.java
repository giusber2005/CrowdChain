package main.java.house.components;

import java.util.ArrayList;
import java.util.List;

class Blockchain {
    private List<Block> chain;
    private int difficulty;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;

        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        Block genesis = new Block(0, "Genesis Block", "0");
        genesis.mineBlock(difficulty);
        return genesis;
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Transaction data) {
        Block prevBlock = getLatestBlock();
        Block newBlock = new Block(
            prevBlock.getIndex() + 1,
            data,
            prevBlock.getHash()
        );
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            //Check if the current hash is correct
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hash doesn't match calculated hash");
                return false;
            }

            //Check if previous hash matches
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Previous hash doesn't match at all");
                return false;
            }

            String target = new String(new char[difficulty]).replace('\0', '0');
            if (!currentBlock.getHash().substring(0, difficulty).equals(target)) {
                System.out.println("Block not properly mined");
                return false;
            }
        }

        return true;
    }

    public void printChain() {
        System.out.println("Printing the whole blockchain data");
        for (Block block : chain) {
            System.out.println("");
            System.out.println("Block " + block.getIndex() +":");
            System.out.println("\tData:" + block.getData());
            System.out.println("\tHash:" + block.getHash());
            System.out.println("\tPreviousHash:" + block.getPreviousHash());
            System.out.println("\tTimeStamp:" + block.getTimeStamp());
            System.out.println("");
        }
    }

    public List<Block> getChain() {
        return new ArrayList<>(chain);
    }
}