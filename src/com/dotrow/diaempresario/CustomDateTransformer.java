package com.dotrow.diaempresario;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 19/03/14 11:19 AM
 */
public class CustomDateTransformer extends AbstractTransformer implements ObjectFactory {

	SimpleDateFormat simpleDateFormatter;

	public CustomDateTransformer( String dateFormat, TimeZone timeZone ) {
		simpleDateFormatter = new SimpleDateFormat( dateFormat );
		simpleDateFormatter.setTimeZone( timeZone );
	}


	public void transform( Object value ) {
		getContext().writeQuoted( simpleDateFormatter.format( value ) );
	}

	public Object instantiate( ObjectBinder context, Object value, Type targetType, Class targetClass ) {
		try {
			return simpleDateFormatter.parse( value.toString() );
		} catch ( ParseException e ) {
			throw new JSONException( String.format( "Failed to parse %s with %s pattern.", value, simpleDateFormatter.toPattern() ), e );
		}
	}
}
