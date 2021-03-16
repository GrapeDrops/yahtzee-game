package game;

import java.util.Random;

public class YahtzeeGame{
	private int[] dice = new int[5];
	private int[] count = new int[6];
	private int rolls = 1;
	private boolean joker = false;
	
	private int aces = -1;
	private int twos = -1;
	private int threes = -1;
	private int fours = -1;
	private int fives = -1;
	private int sixes = -1;
	private int threeKind = -1;
	private int fourKind = -1;
	private int fullHouse = -1;
	private int smallStraight = -1;
	private int largeStraight = -1;
	private int yahtzee = -1;
	private int chance = -1;
	private int bonus = 0;
	
	Random rand = new Random();
	
	public YahtzeeGame() {
		// Since the first action is always going to be a roll anyways
		// I just choose to make it automatic; the player can still roll twice
		for(int i = 0; i < 5; i++) {
			dice[i] = rand.nextInt(6);
			count[dice[i]]++;
		}
	}
	
	public int rollDie(int index) {
		count[dice[index]]--;
		dice[index] = rand.nextInt(6);
		count[dice[index]]++;
		if(count[dice[index]] == 5) { // Setting up special Yahtzee ruling
			if(yahtzee == 50) {
				bonus += 100;
				rolls = 0;
				return 0;
			}
			else if(yahtzee == 0) {
				switch(dice[index]) {
				case 0:
					if(aces == -1) {
						aces = 5;
						rolls = 0;
						return 1;
					}
					else joker = true;
					break;
				case 1:
					if(twos == -1) {
						twos = 10;
						rolls = 0;
						return 2;
					}
					else joker = true;
					break;
				case 2:
					if(threes == -1) {
						threes = 15;
						rolls = 0;
						return 3;
					}
					else joker = true;
					break;
				case 3:
					if(fours == -1) {
						fours = 20;
						rolls = 0;
						return 4;
					}
					else joker = true;
					break;
				case 4:
					if(fives == -1) {
						fives = 25;
						rolls = 0;
						return 5;
					}
					else joker = true;
					break;
				case 5:
					if(sixes == -1) {
						sixes = 30;
						rolls = 0;
						return 6;
					}
					else joker = true;
					break;
				}
			}
		}
		return -1;
	}
	
	public void setDice(int index, int val) {
		dice[index] = val;
	}
	
	public int getDice(int index) {
		return dice[index];
	}
	
	public void setCount(int index, int val) {
		count[index] = val;
	}
	
	public int getCount(int index) {
		return count[index];
	}
	
	public void incrementRoll() {
		rolls++;
	}
	
	public void setRoll(int r) {
		rolls = r;
	}
	
	public int getRoll() {
		return rolls;
	}
	
	public void setJoker(boolean b) {
		joker = b;
	}
	
	public boolean getJoker() {
		return joker;
	}
	
	public void calcAces() {
		if(aces < 0) {
			joker = false;
			aces = count[0];
			rolls = 0;
		}
	}
	
	public void setAces(int a) {
		aces = a;
	}
	
	public int getAces() {
		return aces;
	}
	
	public void calcTwos() {
		if(twos < 0) {
			joker = false;
			twos = count[1]*2;
			rolls = 0;
		}
	}
	
	public void setTwos(int t) {
		twos = t;
	}
	
	public int getTwos() {
		return twos;
	}
	
	public void calcThrees() {
		if(threes < 0) {
			joker = false;
			threes = count[2]*3;
			rolls = 0;
		}
	}
	
	public void setThrees(int t) {
		threes = t;
	}
	
	public int getThrees() {
		return threes;
	}
	
	public void calcFours() {
		if(fours < 0) {
			joker = false;
			fours = count[3]*4;
			rolls = 0;
		}
	}
	
	public void setFours(int f) {
		fours = f;
	}
	
	public int getFours() {
		return fours;
	}
	
	public void calcFives() {
		if(fives < 0) {
			joker = false;
			fives = count[4]*5;
			rolls = 0;
		}
	}
	
	public void setFives(int f) {
		fives = f;
	}
	
	public int getFives() {
		return fives;
	}
	
	public void calcSixes() {
		if(sixes < 0) {
			joker = false;
			sixes = count[5]*6;
			rolls = 0;
		}
	}
	
	public void setSixes(int s) {
		sixes = s;
	}
	
	public int getSixes() {
		return sixes;
	}
	
	public void calcThreeKind() {
		if(threeKind < 0) {
			joker = false;
			if(count[0] >= 3 || count[1] >= 3 || count[2] >= 3 || count[3] >= 3 || count[4] >= 3 || count[5] >= 3) {
				threeKind = dice[0]+dice[1]+dice[2]+dice[3]+dice[4]+5;
			}
			else threeKind = 0;
			rolls = 0;
		}
	}
	
	public void setThreeKind(int t) {
		threeKind = t;
	}
	
	public int getThreeKind() {
		return threeKind;
	}
	
	public void calcFourKind() {
		if(fourKind < 0) {
			joker = false;
			if(count[0] >= 4 || count[1] >= 4 || count[2] >= 4 || count[3] >= 4 || count[4] >= 4 || count[5] >= 4) {
				fourKind = dice[0]+dice[1]+dice[2]+dice[3]+dice[4]+5;
			}
			else fourKind = 0;
			rolls = 0;
		}
	}
	
	public void setFourKind(int f) {
		fourKind = f;
	}
	
	public int getFourKind() {
		return fourKind;
	}
	
	public void calcFullHouse() {
		if(fullHouse < 0) {
			boolean pair = false;
			boolean trio = false;
			for(int i : count) {
				if(i == 2) pair = true;
				else if(i == 3) trio = true;
			}
			if(joker || (pair && trio)) {
				joker = false;
				fullHouse = 25;
			}
			else fullHouse = 0;
			rolls = 0;
		}
	}
	
	public void setFullHouse(int f) {
		fullHouse = f;
	}
	
	public int getFullHouse() {
		return fullHouse;
	}
	
	public void calcSmallStraight() {
		if(smallStraight < 0) {
			boolean ss = false;
			int i = 0;
			for(int j : count) {
				if(j > 0) {
					if(++i == 4) ss = true;
				}
				else i = 0;
			}
			if(joker || ss) {
				joker = false;
				smallStraight = 30;
			}
			else smallStraight = 0;
			rolls = 0;
		}
	}
	
	public void setSmallStraight(int s) {
		smallStraight = s;
	}
	
	public int getSmallStraight() {
		return smallStraight;
	}
	
	public void calcLargeStraight() {
		if(largeStraight < 0) {
			boolean ls = false;
			int i = 0;
			for(int j : count) {
				if(j > 0) {
					if(++i == 5) ls = true;
				}
				else i = 0;
			}
			if(joker || ls) {
				joker = false;
				largeStraight = 40;
			}
			else largeStraight = 0;
			rolls = 0;
		}
	}
	
	public void setLargeStraight(int l) {
		largeStraight = l;
	}
	
	public int getLargeStraight() {
		return largeStraight;
	}
	
	public void calcYahtzee() {
		if(yahtzee < 0) {
			yahtzee = 0;
			for(int i : count) {
				if(i == 5) yahtzee = 50;
			}
			rolls = 0;
		}
	}
	
	public void setYahtzee(int y) {
		yahtzee = y;
	}
	
	public int getYahtzee() {
		return yahtzee;
	}
	
	public void calcChance() {
		if(chance < 0) {
			joker = false;
			chance = 0;
			for(int i : dice) {
				chance += i+1;
			}
			rolls = 0;
		}
	}
	
	public void setChance(int c) {
		chance = c;
	}
	
	public int getChance() {
		return chance;
	}
	
	public void setBonus(int b) {
		bonus = b;
	}
	
	public int getBonus() {
		return bonus;
	}
}
