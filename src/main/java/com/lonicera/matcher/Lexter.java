package com.lonicera.matcher;

import com.lonicera.token.Token;

public interface Lexter{
	Token nextToken();
	Token lookAhead();
}
