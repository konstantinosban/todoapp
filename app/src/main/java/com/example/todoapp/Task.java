package com.example.todoapp;

//Δηλωση της κλασης και των πεδιων των εργασιων
public class Task {
    private String description; //περιγραφή της εργασίας
    private boolean isDone; // true/false αν έχει ολοκληρωθεί

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; } //ορίζει αν είναι ολοκληρωμένο
    public void setDescription(String description) { this.description = description; } //αλλάζει την περιγραφή
}
