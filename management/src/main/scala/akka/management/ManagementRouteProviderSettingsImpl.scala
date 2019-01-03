/*
 * Copyright (C) 2017-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.management

import akka.annotation.InternalApi
import akka.http.scaladsl.model.Uri
import akka.management.http.ManagementRouteProviderSettings

@InternalApi
final case class ManagementRouteProviderSettingsImpl(
    override val selfBaseUri: Uri
) extends ManagementRouteProviderSettings
