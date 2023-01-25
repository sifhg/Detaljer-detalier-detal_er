import java.util.concurrent.ThreadLocalRandom;

public class App {
    
    static String VALID_CHARS = "abcdefghijklmnopqrstuvwxyzæøå ";
    public static void main(String[] args) throws Exception {
        String seedName = "   Sif Høg   ";
		String goalName = "Eleanor Scott";
		String mutation;
        int iteration = 0;

        System.out.println(iteration + " " + seedName + "; " + getFitness(seedName, goalName));
        while(getFitness(seedName, goalName) < 1) {
            mutation = mutate(seedName);
            if(getFitness(goalName, mutation) > getFitness(goalName, seedName)) {
                seedName = mutation;
                System.out.println(iteration + " " + seedName + "; " + getFitness(seedName, goalName));
            }
            iteration++;
        }
        System.out.println(iteration + " " + seedName + "; " + getFitness(seedName, goalName));
    }
    static float getFitness(String seedInput, String goalInput) {
		float combintionFitness;
		float permutationFitness;
        String seed = seedInput.toLowerCase();
        String goal = goalInput.toLowerCase();

        combintionFitness = 1;
        for(int c = 0; c < goal.length(); c++) {
            if(seed.indexOf(goal.charAt(c)) == -1) {
                combintionFitness += 1;
            }else {
                seed = delete(seed, seed.indexOf(goal.charAt(c))); 
            }
        }

        for(int c = 0; c < seed.length(); c++) {
            if(goal.indexOf(seed.charAt(c)) == -1) {
                combintionFitness++;
            }else {
                goal = delete(goal, goal.indexOf(seed.charAt(c))); 
            }
        }
        combintionFitness = 1/combintionFitness;
        seed = seedInput.toLowerCase();
        goal = goalInput.toLowerCase();

        permutationFitness = 0;
        String shortName = (goal.length() < seed.length()) ? goal : seed;
        String longName = (goal.length() < seed.length()) ? seed : goal;
        for(int kernSize = 0; kernSize < (longName.length()/2)-1; kernSize++) {
            for(int c = kernSize; c < shortName.length()-kernSize; c++) {
                if(shortName.charAt(c) == longName.charAt(c+kernSize) || shortName.charAt(c) == longName.charAt(c-kernSize)) {
                    permutationFitness += 1/((float)shortName.length()*(kernSize+1));
                }
            }
        }

		return combintionFitness * permutationFitness;
	}

    static String mutate(String input) {
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 4);
        int randomIndex;
        int randomIndex0;
        int randomIndex1;
        char randomChar;

        switch(randomNumber) {
            case 0:
                randomIndex0 = ThreadLocalRandom.current().nextInt(0, input.length());
                randomIndex1 = ThreadLocalRandom.current().nextInt(0, input.length());
                return swap(input, randomIndex0, randomIndex1);
            case 1:
                randomIndex = ThreadLocalRandom.current().nextInt(0, input.length());
                randomChar = VALID_CHARS.charAt(ThreadLocalRandom.current().nextInt(0, VALID_CHARS.length()));
                return change(input, randomIndex, randomChar);
            case 2:
                randomIndex = ThreadLocalRandom.current().nextInt(0, input.length());
                randomChar = VALID_CHARS.charAt(ThreadLocalRandom.current().nextInt(0, VALID_CHARS.length()));
                return add(input, randomIndex, randomChar);
            case 3:
                randomIndex = ThreadLocalRandom.current().nextInt(0, input.length());
                return delete(input, randomIndex);
        }
        System.out.println("ERROR: randomNumber for mutation must be an int from >= 0 and <4");
        System.out.println("ERROR: randomNumber = " + randomNumber);
        System.exit(0);
        return "ERROR";
    }
    static String swap(String input, int index0, int index1) {
		StringBuilder temp = new StringBuilder(input);
		temp.setCharAt(index0, input.charAt(index1));
		temp.setCharAt(index1, input.charAt(index0));
		return temp.toString();
	}
    static String change(String theInput, int index, char newChar) {
		StringBuilder input = new StringBuilder(theInput);
		input.setCharAt(index, newChar);
		return input.toString();
	}
    static String add(String input,int index, char theChar) {
		StringBuilder temp = new StringBuilder(input);
		temp.insert(index, theChar);
		return temp.toString();
	}
    static String delete(String theInput, int index) {
		StringBuilder input = new StringBuilder(theInput);
		if(theInput.length() > 0){
			input = input.deleteCharAt(index);
		}
		return input.toString();
	}
}
