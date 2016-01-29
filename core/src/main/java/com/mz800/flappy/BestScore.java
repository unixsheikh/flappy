package com.mz800.flappy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class BestScore implements Externalizable {
    private static final String TAG = BestScore.class.getSimpleName();

    private int score;
    private int lives;
    private int attempts;
    private long date;
    private String playerId;

    @Override
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        score = input.readInt();
        lives = input.readInt();
        attempts = input.readInt();
        date  = input.readLong();
        playerId = (String) input.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(score);
        output.writeInt(lives);
        output.writeInt(attempts);
        output.writeLong(date);
        output.writeObject(playerId);
    }

    public BestScore(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        readExternal(stream);
    }

    public BestScore(int score, int lives, int attempts, long date, String playerId) {
        this.score = score;
        this.lives = lives;
        this.attempts = attempts;
        this.date = date;
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getAttempts() {
        return attempts;
    }

    public long getDate() {
        return date;
    }

    public String getPlayerId() {
        return playerId;
    }
}