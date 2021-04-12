package ru.codercat
package utils

import org.http4s.Status

sealed trait Errors

case class HttpError(status: Status) extends Throwable with Errors
