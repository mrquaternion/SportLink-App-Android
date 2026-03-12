package com.mlr.sportlink.utilities

// Mechanical first-pass port generated from Utilities/GeoMath.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import com.mlr.sportlink.core.models.local.GeoCoordinate
import com.mlr.sportlink.core.models.local.Park
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.PI

private const val METERS_PER_DEGREE = 111_320.0

data class CoordinateSpan(
    val latitudeDelta: Double,
    val longitudeDelta: Double,
)

data class MapRegion(
    val center: GeoCoordinate,
    val span: CoordinateSpan,
)

fun localOffset(point: GeoCoordinate, relativeTo: GeoCoordinate): Pair<Double, Double> {
    val cosLatRef = cos(relativeTo.latitude * PI / 180)
    val x = (point.longitude - relativeTo.longitude) * cosLatRef * METERS_PER_DEGREE
    val y = (point.latitude - relativeTo.latitude) * METERS_PER_DEGREE
    return x to y
}

fun withinLimits(point: GeoCoordinate, poly: List<GeoCoordinate>): Boolean {
    if (poly.size <= 1) return false

    var inside = false
    var j = poly.lastIndex
    for (i in poly.indices) {
        val xi = poly[i].longitude
        val yi = poly[i].latitude
        val xj = poly[j].longitude
        val yj = poly[j].latitude

        val intersects = ((yi > point.latitude) != (yj > point.latitude)) &&
            (point.longitude < (xj - xi) * (point.latitude - yi) / ((yj - yi).takeIf { abs(it) > 1e-9 } ?: 1e-9) + xi)
        if (intersects) inside = !inside
        j = i
    }
    return inside
}

fun polygonCenter(arr: List<GeoCoordinate>): GeoCoordinate {
    if (arr.size <= 2) return arr.firstOrNull() ?: GeoCoordinate(0.0, 0.0)

    val ref = arr.first()
    val cosLatRef = cos(ref.latitude * PI / 180)
    val pointsXY = arr.map { localOffset(it, ref) }

    var area = 0.0
    for (i in pointsXY.indices) {
        val j = (i + 1) % pointsXY.size
        area += pointsXY[i].first * pointsXY[j].second - pointsXY[j].first * pointsXY[i].second
    }
    area *= 0.5

    var cx = 0.0
    var cy = 0.0
    for (i in pointsXY.indices) {
        val j = (i + 1) % pointsXY.size
        val cross = pointsXY[i].first * pointsXY[j].second - pointsXY[j].first * pointsXY[i].second
        cx += (pointsXY[i].first + pointsXY[j].first) * cross
        cy += (pointsXY[i].second + pointsXY[j].second) * cross
    }
    cx /= (6 * area)
    cy /= (6 * area)

    return GeoCoordinate(
        latitude = cy / METERS_PER_DEGREE + ref.latitude,
        longitude = cx / (cosLatRef * METERS_PER_DEGREE) + ref.longitude,
    )
}

fun computedBoundingRegion(coords: List<GeoCoordinate>, factor: Double = 1.2): MapRegion? {
    if (coords.isEmpty()) return null

    var minLat = coords.first().latitude
    var maxLat = coords.first().latitude
    var minLon = coords.first().longitude
    var maxLon = coords.first().longitude

    coords.forEach { coord ->
        minLat = min(minLat, coord.latitude)
        maxLat = max(maxLat, coord.latitude)
        minLon = min(minLon, coord.longitude)
        maxLon = max(maxLon, coord.longitude)
    }

    return MapRegion(
        center = GeoCoordinate(
            latitude = (minLat + maxLat) / 2,
            longitude = (minLon + maxLon) / 2,
        ),
        span = CoordinateSpan(
            latitudeDelta = (maxLat - minLat) * factor,
            longitudeDelta = (maxLon - minLon) * factor,
        ),
    )
}

fun findNearestParks(park: Park, filteredParks: List<Park>, size: Int = 5): MapRegion? {
    val selectedParkCenter = polygonCenter(park.polygon)
    val parkCenters = filteredParks.map { polygonCenter(it.polygon) }
    val nearestParks = nearestPOIs(selectedParkCenter, parkCenters, size)
    return computedBoundingRegion(nearestParks, factor = 1.5)
}

private fun nearestPOIs(
    origin: GeoCoordinate,
    pointOfInterests: List<GeoCoordinate>,
    size: Int,
): List<GeoCoordinate> =
    pointOfInterests
        .map { point ->
            val offset = localOffset(point, origin)
            val distanceSquared = offset.first * offset.first + offset.second * offset.second
            point to distanceSquared
        }
        .sortedBy { it.second }
        .take(size)
        .map { it.first }
