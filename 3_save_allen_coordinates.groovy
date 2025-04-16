import net.imglib2.RealPoint
import qupath.lib.measurements.MeasurementList
import qupath.ext.biop.abba.AtlasTools
import qupath.lib.gui.tools.MeasurementExporter
import qupath.lib.objects.PathDetectionObject

import static qupath.lib.gui.scripting.QPEx.*

/**
 * Computes the centroid coordinates of each detection within the atlas
 * then adds these coordinates onto the measurement list.
 * Measurements names: "Allen CCFv3 X", "Allen CCFv3 Y", "Allen CCFv3 Z"
 */

def pixelToAtlasTransform = 
    AtlasTools
    .getAtlasToPixelTransform(getCurrentImageData())
    .inverse() // pixel to atlas = inverse of atlas to pixel

getDetectionObjects().forEach(detection -> {
    RealPoint atlasCoordinates = new RealPoint(3);
    MeasurementList ml = detection.getMeasurementList();
    atlasCoordinates.setPosition([detection.getROI().getCentroidX(),detection.getROI().getCentroidY(),0] as double[]);
    pixelToAtlasTransform.apply(atlasCoordinates, atlasCoordinates);
    ml.putMeasurement("Allen CCFv3 X mm", atlasCoordinates.getDoublePosition(0) )
    ml.putMeasurement("Allen CCFv3 Y mm", atlasCoordinates.getDoublePosition(1) )
    ml.putMeasurement("Allen CCFv3 Z mm", atlasCoordinates.getDoublePosition(2) )
})
