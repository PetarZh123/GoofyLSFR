// Our implementation of fibonacci linear-feedback shift register
// producing pseudo-random 16 bit string and using it to output
// true/false value with a certain probability (the only argument of the method)
// Arg value is a floating point between 0 and 1 (probability of event)

public boolean RandomBool(double chance){
    //ArgumentException for arg not being in the interval (0,1)
    if (!(chance>0) || !(chance<1)){
        throw new IllegalArgumentException("Method argument must be a floating point number between 0 and 1 (excl.) with max 15 decimals");
    }

    //seed derrived from the milliseconds of System.currentTimeMillis
    boolean[] seed = new boolean[16];

    //Obtaining a number from 1 to 2^16 = 65536 and its binary representation
    StringBuilder currentMilliseconds = new StringBuilder(Integer.toString((int) (System.currentTimeMillis() % 65536 + 1), 2));

    //Making currentMilliseconds string 16 bits in case it is less than 16
    while (currentMilliseconds.length()<16){
        currentMilliseconds.insert(0, '0');
    }

    //setting initial state (seed)
    for (int i = 0; i < 16; i++) {
        seed[i] = currentMilliseconds.charAt(i) == '1';
    }

    //state generating random number
    boolean[] state = seed.clone();

    //doing a 16-step cycle to generate a whole
    //new state different from the seed
    for (int i = 0; i < 16; i++) {
        //first bit XOR calculation (16-th, 14-th, 13-th and 11-th bit are tap-bits)
        boolean firstBit;
        if(((state[15]^state[13])
                ^state[12])
                ^state[10]) {
            firstBit = true;
        }
        else {
            firstBit = false;
        }

        //changing state (right-shift)
        for (int j = 15; j > 0; j--) {
            state[j] = state[j-1];
        }
        state[0] = firstBit;
    }

    //doing the same 16 step cycle with the purpose
    //of obtating a pseudo-random 16-bit number
    boolean[] output = new boolean[16];
    for (int i = 0; i < 16; i++) {
        output[i] = state[15];

        boolean firstBit;
        if(((state[15]^state[13])
                ^state[12])
                ^state[10])
        {
            firstBit = true;
        }
        else
        {
            firstBit = false;
        }

        for (int j = 15; j > 0; j--) {
            state[j] = state[j-1];
        }
        state[0] = firstBit;
    }

    //calculating random number
    int randomInt = 0;
    for (int i = 15; i > -1; i--) {
        if (output[i]) {
            randomInt += Math.pow(2,i);
        }
    }

    //Since randomInt is 'random' the chance of
    //it being smaller than 2^16/(initial argument) is
    //approximately the initial argument itself
    return randomInt < (65536*chance);
}


//The only purpose of the main method
//Is to prove that the distribution of
//The output is sufficiently 'random'
public void main() {
    double chance = 0.2;

    int trueCounter = 0, falseCounter = 0;
    for (int i = 0; i < 1000; i++) {
        System.out.println(RandomBool(chance));
        if (RandomBool(chance)) {
            trueCounter++;
        }else {
            falseCounter++;
        }
        try{Thread.sleep(7);}catch(InterruptedException e){System.out.println(e);}
    }
    System.out.println("False count: " + falseCounter + " True count: " + trueCounter);
    System.out.println("Chosen probability: " + chance);
}