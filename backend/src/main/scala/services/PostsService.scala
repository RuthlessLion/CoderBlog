package ru.codercat
package services

import cats.effect.IO
import api.posts.Model.Post

class PostsService extends api.posts.Api[IO, IO] {
  private val testPosts = List(
    Post(title = "Пост #1", body = "В этом году мы несколько раз писали об успехах китайских разработчиков оборудования для производства чипов. Поднебесная после начала торговой войны с США стала очень активно внедрять импортозамещение, причем достаточно успешно.\n\nНапример, в январе стало известно о том, что китайские компании разрабатывают собственные процессоры для мобильных устройств и ноутбуков с десктопами. Вполне может быть, что в ближайшем будущем эти компании станут опасными конкурентами таких гигантов, как TSMC, Samsung и других. Ну а сейчас появилась новость о том, что китайская компания поставляет собственное оборудование для производства 5-нм чипов крупнейшим производителям мира.\n\nРечь идет об AMEC, ее руководитель Джеральд Инь на отчетной конференции заявил, что «ряд мировых лидеров по производству 5-нм чипов» используют промышленные установки компании. К сожалению, информация о том, кто именно закупил оборудование, не раскрывается. Но и особой загадки нет, поскольку именно лидеров в этом направлении не так много — это либо Samsung, либо TSMC, либо обе компании вместе.", id = "1"),
    Post(title = "Пост #2", body = "Тестовый текст поста.", id = "2")
  )

  override def getPosts(numb: Option[Long], limit: Option[Long]): IO[List[Post]] = IO.pure{
    val numbPosts = numb.map(_.toInt).map(testPosts.take).getOrElse(testPosts)
    numbPosts.map(
      post => post.copy(body = limit.map(limit => post.body.split(' ').reduce(
        (acc, word) =>
          if(s"$acc $word".length > limit) acc
          else s"$acc $word"
      )).getOrElse(post.body))
    )}
}

object PostsService {
  def apply() = new api.posts.Http4sServer[IO](new PostsService).toHttpApp
}


