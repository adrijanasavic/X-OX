package com.example.x_ox;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1, textViewPlayer2;
    private Button btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        textViewPlayer1 = findViewById( R.id.text_view_p1 );
        textViewPlayer2 = findViewById( R.id.text_view_p2 );
        btn_reset = findViewById( R.id.btn_reset );

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                String buttonId = "btn_" + i + j;
                int resId = getResources().getIdentifier( buttonId, "id", getPackageName() );
                buttons[i][j] = findViewById( resId );
                buttons[i][j].setOnClickListener( this );
            }
        }

        btn_reset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        } );
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().trim().equals( "" )) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText( "O" );
        } else {
            ((Button) v).setText( "X" );
        }

        roundCount++;
        if (checkForWin()) {
            if (player1Turn) {
                if ((player1Points == 0 || player2Points == 0) && roundCount == 2) {
                    Toast.makeText( this, "PLAYER 2 WON!", Toast.LENGTH_SHORT ).show();
                    resetGame();
                    return;
                }
                player1Wins();
            } else {
                if ((player1Points == 0 || player2Points == 0) && roundCount == 2) {
                    Toast.makeText( this, "PLAYER 1 WON!", Toast.LENGTH_SHORT ).show();
                    resetGame();
                    return;
                }
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals( field[i][1] )
                    && field[i][0].equals( field[i][2] )
                    && !field[i][0].equals( "" )) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals( field[1][i] )
                    && field[0][i].equals( field[2][i] )
                    && !field[0][i].equals( "" )) {
                return true;
            }
        }
        if (field[0][0].equals( field[1][1] )
                && field[0][0].equals( field[2][2] )
                && !field[0][0].equals( "" )) {
            return true;
        }
        if (field[2][0].equals( field[1][1] )
                && field[2][0].equals( field[0][2] )
                && !field[2][0].equals( "" )) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText( this, "Player 1 wins", Toast.LENGTH_SHORT ).show();
        textViewPlayer1.setText( "Player 1: " + (player1Points) );
        resetBoard();

    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText( this, "Player 2 wins", Toast.LENGTH_SHORT ).show();
        resetBoard();
        textViewPlayer2.setText( "Player 2: " + (player2Points) );
    }

    private void draw() {
        Toast.makeText( this, "Draw!", Toast.LENGTH_SHORT ).show();
        resetBoard();
    }

    private void resetBoard() {

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                buttons[i][j].setText( "" );
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        resetBoard();
        textViewPlayer1.setText( "Player 1: 0" );
        textViewPlayer2.setText( "Player 2: 0" );
        player2Points = 0;
        player1Points = 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );

        outState.putInt( "roundCount", roundCount );
        outState.putInt( "player1Points", player1Points );
        outState.putInt( "player2Points", player2Points );
        outState.putBoolean( "player1Turn", player1Turn );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );

        roundCount = savedInstanceState.getInt( "roundCount" );
        player1Points = savedInstanceState.getInt( "player1Points" );
        player2Points = savedInstanceState.getInt( "player2Points" );
        player1Turn = savedInstanceState.getBoolean( "player1Turn" );
    }
}
