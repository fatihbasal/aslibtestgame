package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import javax.swing.text.View;

public class FlippyBirb extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture character;
	Texture barrier;
	Texture barrier2;
	Texture barrier3;
	float characterX = 0;
	float characterY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.8f;
	Random random;
	Circle characterCircle;


	int numberOfBarriers = 4;
	float[] barrierX = new float[numberOfBarriers];
	float[] barrierOffset = new float[numberOfBarriers];
	float[] barrierOffset2 = new float[numberOfBarriers];
	float[] barrierOffset3 = new float[numberOfBarriers];
	Circle[] barrierCircle;
	Circle[] barrierCircle2;
	Circle[] barrierCircle3;
	ShapeRenderer shapeRenderer;
	float distance = 0;
	float barrierVelocity = 10;
	int score = 0;
	int scoredBarrier = 0;
	BitmapFont font;
	BitmapFont gameOverFont;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		character = new Texture("character.png");
		barrier = new Texture("barrier.png");
		barrier2 = new Texture("barrier.png");
		barrier3 = new Texture("barrier.png");
		characterX = Gdx.graphics.getWidth()/4;characterY = Gdx.graphics.getHeight()/3;
		distance =  Gdx.graphics.getWidth()/2;

		characterCircle = new Circle();

		barrierCircle = new Circle[numberOfBarriers];
		barrierCircle2 = new Circle[numberOfBarriers];
		barrierCircle3 = new Circle[numberOfBarriers];

		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.BLUE);
		font.getData().setScale(4);

		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.DARK_GRAY);
		gameOverFont.getData().setScale(6);

		random = new Random();

		for(int i = 0; i < barrierX.length; i++)
		{
			barrierOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			barrierOffset2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			barrierOffset3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			barrierX[i] = Gdx.graphics.getWidth() - (barrier.getWidth()/2) + (i*distance);

			barrierCircle[i] = new Circle();
			barrierCircle2[i] = new Circle();
			barrierCircle3[i] = new Circle();

		}

	}



	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


	if(gameState == 1)
	{
		if(barrierX[scoredBarrier]< Gdx.graphics.getWidth()/4)
		{
			score++;
			if(scoredBarrier < numberOfBarriers - 1)
			{
				scoredBarrier++;
			}
			else
			{
				scoredBarrier = 0;
			}
		}
		if(Gdx.input.justTouched())
		{
			velocity = -20;
		}


		for(int i = 0; i < barrierX.length; i++)
		{
			if(barrierX[i] < Gdx.graphics.getWidth()/20)
			{
				barrierX[i] = barrierX[i] +numberOfBarriers*distance;
				barrierOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				barrierOffset2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				barrierOffset3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			}
			else
			{
				barrierX[i] = barrierX[i] - barrierVelocity;
			}


			batch.draw(barrier,barrierX[i],Gdx.graphics.getHeight()/2+barrierOffset[i],Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/10);
			batch.draw(barrier2,barrierX[i],Gdx.graphics.getHeight()/2+barrierOffset2[i],Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/10);
			batch.draw(barrier3,barrierX[i],Gdx.graphics.getHeight()/2+barrierOffset3[i],Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/10);


			barrierCircle[i] = new Circle(barrierX[i] +Gdx.graphics.getWidth()/40,Gdx.graphics.getHeight()/2+barrierOffset[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
			barrierCircle2[i] = new Circle(barrierX[i] +Gdx.graphics.getWidth()/40,Gdx.graphics.getHeight()/2+barrierOffset2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
			barrierCircle3[i] = new Circle(barrierX[i] +Gdx.graphics.getWidth()/40,Gdx.graphics.getHeight()/2+barrierOffset3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);


		}

		if(characterY > 0 && characterY < Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/22)
		{
			velocity+= gravity;
			characterY -= velocity;
		}
		else
		{
			gameState = 2;
		}


	}
	else if(gameState == 0)
	{
		if(Gdx.input.justTouched())
		{
			gameState = 1;
		}
	}
	else if(gameState == 2)
	{
		gameOverFont.draw(batch,"Game Over! Tap to Play Again!",Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/2);
		if(Gdx.input.justTouched())
		{
			gameState = 1;
			this.characterY = Gdx.graphics.getHeight()/3;

			for(int i = 0; i < barrierX.length; i++)
			{
				barrierOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				barrierOffset2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				barrierOffset3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

				barrierX[i] = Gdx.graphics.getWidth() - (barrier.getWidth()/2) + (i*distance);

				barrierCircle[i] = new Circle();
				barrierCircle2[i] = new Circle();
				barrierCircle3[i] = new Circle();

			}
		velocity = 0;
			score = 0;
			scoredBarrier = 0;
		}
	}


	batch.draw(character,characterX,characterY,Gdx.graphics.getWidth()/22,Gdx.graphics.getHeight()/11);
	font.draw(batch,String.valueOf(score),100,200);
	batch.end();

	characterCircle.set(characterX+Gdx.graphics.getWidth()/44,characterY+Gdx.graphics.getHeight()/22,Gdx.graphics.getWidth()/44);
	//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	//shapeRenderer.setColor(Color.BLACK);
	//shapeRenderer.circle(characterCircle.x,characterCircle.y,characterCircle.radius);

	for(int i = 0; i < barrierOffset.length; i++)
	{
		//shapeRenderer.circle(barrierX[i] +Gdx.graphics.getWidth()/40,Gdx.graphics.getHeight()/2+barrierOffset[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
		//shapeRenderer.circle(barrierX[i] +Gdx.graphics.getWidth()/40,Gdx.graphics.getHeight()/2+barrierOffset2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
		//shapeRenderer.circle(barrierX[i] +Gdx.graphics.getWidth()/40,Gdx.graphics.getHeight()/2+barrierOffset3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);

		if(Intersector.overlaps(characterCircle,barrierCircle[i]) || Intersector.overlaps(characterCircle,barrierCircle2[i]) ||
		Intersector.overlaps(characterCircle,barrierCircle3[i]))
		{
			gameState = 2;
		}

	}
	shapeRenderer.end();

	}
	
	@Override
	public void dispose () {

	}
}
