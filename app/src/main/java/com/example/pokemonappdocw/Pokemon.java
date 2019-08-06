package com.example.pokemonappdocw;

public class Pokemon {
    private String imageURL, legendary, pokemonName, type1, type2, description, move1, move2;
    private int attack, defense, generation, hp, number, specialAttack, specialDefence, speed, total, level, caughtAtStep;

    public Pokemon(){

    }

    public Pokemon(String imageURL, String legendary, String pokemonName, String type1, String type2, int attack, int defense, int generation, int hp, int number, int specialAttack, int specialDefence, int speed, int total, String description, String move1, String move2, int level, int caughtAtStep) {
        this.imageURL = imageURL;
        this.legendary = legendary;
        this.pokemonName = pokemonName;
        this.type1 = type1;
        this.type2 = type2;
        this.attack = attack;
        this.defense = defense;
        this.generation = generation;
        this.hp = hp;
        this.number = number;
        this.specialAttack = specialAttack;
        this.specialDefence = specialDefence;
        this.speed = speed;
        this.total = total;
        this.description = description;
        this.move1 = move1;
        this.move2 = move2;
        this.level = level;
        this.caughtAtStep = caughtAtStep;
    }

    public int getCaughtAtStep() {
        return caughtAtStep;
    }

    public void setCaughtAtStep(int caughtAtStep) {
        this.caughtAtStep = caughtAtStep;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLegendary() {
        return legendary;
    }

    public void setLegendary(String legendary) {
        this.legendary = legendary;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefence() {
        return specialDefence;
    }

    public void setSpecialDefence(int specialDefence) {
        this.specialDefence = specialDefence;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMove1() {
        return move1;
    }

    public void setMove1(String move1) {
        this.move1 = move1;
    }

    public String getMove2() {
        return move2;
    }

    public void setMove2(String move2) {
        this.move2 = move2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}


