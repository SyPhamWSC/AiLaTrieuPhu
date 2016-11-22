package com.example.leojr.ailatrieuphu.play;


public class Score {
    private int score;
    private int level;
    private boolean checkStopGame;
    public Score(int level, boolean checkStopGame){
        this.level = level;
        this.checkStopGame = checkStopGame;
    }
    public int returnScore(){
        if(checkStopGame){
            switch (level){
                case 0:
                    score = 0;
                    break;
                case 1:
                    score = 200000;
                    break;
                case 2:
                    score = 400000;
                    break;
                case 3:
                    score = 600000;
                    break;
                case 4:
                    score = 1000000;
                    break;
                case 5:
                    score = 2000000;
                    break;
                case 6:
                    score = 3000000;
                    break;
                case 7:
                    score = 6000000;
                    break;
                case 8:
                    score = 10000000;
                    break;
                case 9:
                    score = 14000000;
                    break;
                case 10:
                    score = 22000000;
                    break;
                case 11:
                    score = 30000000;
                    break;
                case 12:
                    score = 40000000;
                    break;
                case 13:
                    score = 60000000;
                    break;
                case 14:
                    score = 85000000;
                    break;
                case 15:
                    score = 150000000;
                    break;
            }
        } else if(level < 5){
            score = 0;
        } else if(level >= 5 && level < 10){
            score = 2000000;
        } else if(level >=10 && level< 15){
            score = 22000000;
        } else if (level ==15){
            score = 150000000;
        }
        return score;
    }



}
