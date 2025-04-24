package com.example.poo.utils;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

/**
 * A class that represents all the button used
 */
public class GameButton extends Button {

    private final String buttonPressed = "-fx-background-color: transparent; -fx-background-image: url('pressedButton.png');";
    private final String buttonReleased = "-fx-background-color: transparent; -fx-background-image: url('releasedButton.png');";

    /**
     * Constructor for a button
     * @param buttonText The text to be displayed
     */
    public GameButton(String buttonText)
    {
        setText(buttonText);
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(buttonReleased);
        setButtonListener();
    }

    /**
     * Sets the button display mode to be pressed
     */
    private void setPressed() {
        setStyle(buttonPressed);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }

    /**
     * Sets the button display mode to be released
     */
    private void setReleased() {
        setStyle(buttonReleased);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }

    /**
     * Listener for an action on the button
     */
    private void setButtonListener() {

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setPressed();
                }
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setReleased();
                }
            }
        });

    }
}
