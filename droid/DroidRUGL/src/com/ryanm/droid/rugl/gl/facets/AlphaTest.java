
package com.ryanm.droid.rugl.gl.facets;

import com.ryanm.droid.rugl.gl.Facet;
import com.ryanm.droid.rugl.gl.enums.ComparisonFunction;

import android.opengl.GLES10;

/**
 * Controls the alpha test
 * 
 * @author ryanm
 */
public class AlphaTest extends Facet<AlphaTest>
{
	/**
	 * If the alpha test is enabled or not
	 */
	public final boolean enabled;

	/**
	 * The alpha function
	 */
	public final ComparisonFunction func;

	/**
	 * The reference value
	 */
	public final float ref;

	/**
	 * Use this to disable the alpha test
	 */
	public static final AlphaTest disabled = new AlphaTest();

	/**
	 * Alpha test is disabled
	 */
	private AlphaTest()
	{
		enabled = false;
		func = ComparisonFunction.ALWAYS;
		ref = 0;
	}

	/**
	 * Alpha test is enabled
	 * 
	 * @param func
	 * @param ref
	 */
	public AlphaTest( ComparisonFunction func, float ref )
	{
		enabled = true;
		this.func = func;
		this.ref = ref;
	}

	@Override
	public void transitionFrom( AlphaTest a )
	{
		if( enabled && !a.enabled )
		{
			GLES10.glEnable( GLES10.GL_ALPHA_TEST );
		}
		else if( !enabled && a.enabled )
		{
			GLES10.glDisable( GLES10.GL_ALPHA_TEST );
		}

		if( enabled && ( func != a.func || ref != a.ref ) )
		{
			GLES10.glAlphaFunc( func.value, ref );
		}
	}

	@Override
	public int compareTo( AlphaTest a )
	{
		float d = ( enabled ? 1 : 0 ) - ( a.enabled ? 1 : 0 );

		if( d == 0 )
		{
			d = func.ordinal() - a.func.ordinal();

			if( d == 0 )
			{
				d = ref - a.ref;
			}
		}

		return ( int ) Math.signum( d );
	}

	@Override
	public String toString()
	{
		return "Alpha test " + enabled + " " + func + " " + ref;
	}
}
