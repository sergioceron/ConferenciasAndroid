package com.dotrow.diaempresario.social;

import com.dotrow.diaempresario.HTTPResponse;

import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 10/02/14 09:49 PM
 */
public class PostJson extends HTTPResponse {
	private List<Post> posts;

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts( List<Post> posts ) {
		this.posts = posts;
	}
}
