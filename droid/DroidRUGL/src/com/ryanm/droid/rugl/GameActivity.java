
package com.ryanm.droid.rugl;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.ryanm.droid.config.Configuration;
import com.ryanm.droid.config.VariableType;
import com.ryanm.droid.rugl.config.BoundingRectVarType;
import com.ryanm.droid.rugl.config.ColourVarType;
import com.ryanm.droid.rugl.config.RangeVarType;
import com.ryanm.droid.rugl.config.Vector2fVarType;
import com.ryanm.droid.rugl.config.Vector3fVarType;
import com.ryanm.droid.rugl.res.ResourceLoader;
import com.ryanm.droid.rugl.util.ExceptionHandler;

/**
 * Handy activity that can be simply subclassed. Just remember to call
 * {@link #start(Game, String)} in your
 * {@link #onCreate(android.os.Bundle)} or nothing will happen.
 * Handles starting the {@link ResourceLoader} and key input
 * 
 * @author ryanm
 */
public abstract class GameActivity extends Activity
{
	/***/
	private GameView gameView;

	/**
	 * The {@link Game}
	 */
	protected Game game;

	/**
	 * Call this in your {@link #onCreate(android.os.Bundle)}
	 * implementation
	 * 
	 * @param game
	 * @param supportEmail
	 *           The email address where uncaught exception reports
	 *           should be sent to, or <code>null</code> not to bother
	 */
	public void start( Game game, String supportEmail )
	{
		if( supportEmail != null )
		{
			ExceptionHandler.register( this, supportEmail );
		}

		this.game = game;

		// additional configuration types
		VariableType.register( new ColourVarType() );
		VariableType.register( new RangeVarType() );
		VariableType.register( new Vector2fVarType() );
		VariableType.register( new Vector3fVarType() );
		VariableType.register( new BoundingRectVarType() );

		ResourceLoader.start( getResources() );

		gameView = new GameView( this, game );

		setContentView( gameView );
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		gameView.onPause();
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		// launched from Game.launchConfiguration(). We need to defer
		// application till we're on the OpenGL thread
		Configuration.deferActivityResult( requestCode, resultCode, data );
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		gameView.onResume();
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if( event.getRepeatCount() == 0 )
		{
			gameView.game.currentPhase().onKeyDown( keyCode, event );
		}

		return true;
	}

	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event )
	{
		gameView.game.currentPhase().onKeyUp( keyCode, event );
		return true;
	}
}