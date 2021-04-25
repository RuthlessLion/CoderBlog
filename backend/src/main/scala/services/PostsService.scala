package ru.codercat
package services

import cats.effect.IO
import api.posts.Model.Post
import utils.HttpError
import org.http4s.Status

class PostsService extends api.posts.Api[IO, IO] {
  private val posts = collection.concurrent.TrieMap[String, Post]()

  override def getPosts(page: Option[Long], page_size: Option[Long], limit: Option[Long]): IO[List[Post]] = IO.pure {
    val listPosts = posts.toList.map(_._2)
    val numbPosts = (for {
      pageSizeValue <- page_size
      pageNumbValue <- page
    } yield listPosts.slice(((pageNumbValue - 1) * pageSizeValue).toInt, (((pageNumbValue - 1) * pageSizeValue) + pageSizeValue).toInt)).getOrElse(listPosts)
    numbPosts.map(
      post => post.copy(body = limit.map(limit => post.body.split(' ').reduce(
        (acc, word) =>
          if (s"$acc $word".length > limit) acc
          else s"$acc $word"
      )).getOrElse(post.body))
    )
  }

  override def getPost(id: String): IO[Post] = IO.fromOption(posts.get(id))(HttpError(Status.NotFound))

  override def createPost(body: Post): IO[Unit] =
    if (posts.contains(body.id)) IO.raiseError(HttpError(Status.Conflict))
    else IO.delay(posts.addOne(body.id -> body))
}

object PostsService {
  def apply() = new api.posts.Http4sServer[IO](new PostsService).toHttpApp
}


