import java.util.concurrent.ThreadLocalRandom;

public class App {
    
    static String VALID_CHARS = "abcdefghijklmnopqrstuvwxyzæøå ";
    public static void main(String[] args) throws Exception {
        String seedName = "Sif Høg";
		String goalName = "Eleanor Scott";
        String [] mutations = new String[500];
		
        int iteration = 0;
        while(getFitness(seedName, goalName) < 1) {
            int bestFit = 0;
            for(int i = 0; i < mutations.length; i++) {
                mutations[i] = mutate(mutate(seedName));
                bestFit = (getFitness(mutations[i], goalName) > getFitness(mutations[bestFit], goalName)) ? i : bestFit;
            }
            System.out.println(iteration + " " + seedName + "; " + getFitness(seedName, goalName));
            seedName = mutations[bestFit];
            iteration++;
        }
        System.out.println(iteration + " " + seedName + "; " + getFitness(seedName, goalName));
    }
    static float getFitness(String seedInput, String goalInput) {
		float lengthFitness;
		float combintionFitness;
		float permutationFitness;
        String seed = seedInput.toLowerCase();
        String goal = goalInput.toLowerCase();

		lengthFitness = (float) seed.length() / goal.length();
		if (lengthFitness > 1.0) {
			lengthFitness = 1/lengthFitness;
		}

        combintionFitness = 1;
        for(int c = 0; c < goal.length(); c++) {
            if(seed.indexOf(goal.charAt(c)) == -1) {
                combintionFitness++;
            }
        }
        for(int c = 0; c < seed.length(); c++) {
            if(goal.indexOf(seed.charAt(c)) == -1) {
                combintionFitness++;
            }
        }
        combintionFitness = 1/combintionFitness;

        permutationFitness = 1;
        for(int c = 0; c < seed.length(); c++) {
            float seedPosition = (float) c / seed.length();
            if(goal.indexOf(seed.charAt(c), (int) (seedPosition*goal.length())) >= 0) {
                float goalPosition = (float) goal.indexOf(seed.charAt(c), (int) (seedPosition*0.9*goal.length())) / goal.length();
                float distance = seedPosition - goalPosition;
                distance = (distance > 0) ? distance : -distance;
                permutationFitness += distance;
            }
        }
        for(int c = 0; c < goal.length(); c++) {
            float goalPosition = (float) c / goal.length();
            if(seed.indexOf(goal.charAt(c), (int) (goalPosition*0.9*seed.length())) >= 0) {
                float seedPosition =  (float) seed.indexOf(goal.charAt(c), (int) (goalPosition*0.9*seed.length())) / seed.length();
                float distance = seedPosition - goalPosition;
                distance = (distance > 0) ? distance : -distance;
                permutationFitness += distance;
            }
        }
        permutationFitness = 1/permutationFitness;

		return lengthFitness * combintionFitness * permutationFitness;
	}

    static String mutate(String input) {
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 4);
        int randomIndex;
        char randomChar;

        switch(randomNumber) {
            case 0:
                randomIndex = ThreadLocalRandom.current().nextInt(0, input.length()-1);
                return swap(input, randomIndex);
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
    static String swap(String input, int index) {
		StringBuilder temp = new StringBuilder(input);
		temp.setCharAt(index, input.charAt(index+1));
		temp.setCharAt(index+1, input.charAt(index));
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
