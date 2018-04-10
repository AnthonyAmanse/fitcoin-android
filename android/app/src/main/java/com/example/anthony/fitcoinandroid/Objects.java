package com.example.anthony.fitcoinandroid;

class GetStateFinalResult {
    String contractIds;
    int fitcoinsBalance;
    String id;
    String memberType;
    int stepsUsedForConversion;
    int totalSteps;
}

class BackendResult {
    String status;
    String result;
}

class ResultOfEnroll {
    String message;
    EnrollFinalResult result;
}

class EnrollFinalResult {
    String user;
    String txId;
    String error;
}