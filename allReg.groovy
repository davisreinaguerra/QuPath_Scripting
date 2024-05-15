/**
 * Computes the centroid coordinates of each detection within the atlas
 * then adds these coordinates onto the measurement list.
 * Measurements names: "Atlas_X", "Atlas_Y", "Atlas_Z"
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

import qupath.lib.roi.EllipseROI;
import qupath.lib.objects.PathDetectionObject

points = getAnnotationObjects().findAll{it.getROI().isPoint() }
print points[0].getROI()

describe(points[0].getROI())
//Cycle through each points object (which is a collection of points)
points.each{ 
    //Cycle through all points within a points object
    pathClass = it.getPathClass()
    it.getROI().getAllPoints().each{ 
        //for each point, create a circle on top of it that is "size" pixels in diameter
        x = it.getX()
        y = it.getY()
        size = 5
        def roi = ROIs.createEllipseROI(x-size/2,y-size/2,size,size, ImagePlane.getDefaultPlane())
        
        def aCell = new PathDetectionObject(roi, pathClass)
        addObject(aCell)
    }
}
//remove points if desired.
removeObjects(points, false)
resolveHierarchy()

import net.imglib2.RealPoint
import qupath.lib.measurements.MeasurementList
import qupath.ext.biop.abba.AtlasTools
import qupath.lib.gui.tools.MeasurementExporter
import qupath.lib.objects.PathDetectionObject

import static qupath.lib.gui.scripting.QPEx.* // For intellij editor autocompletion

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



// Get the list of all images in the current project
def project = getProject()
def imagesToExport = project.getImageList()

// Separate each measurement value in the output file with a tab ("\t")
def separator = "\t"

// indicate which measurment columns to include
def columnsToInclude = new String[]{"Allen CCFv3 X mm", "Allen CCFv3 Y mm", "Allen CCFv3 Z mm"}

// indicate export type
def exportType = PathDetectionObject.class

// Choose your *full* output path
def outputPath = "C:/Users/walla/Desktop/QuPath Export/out.tsv"
def outputFile = new File(outputPath)

// Decide all the classes you want to export
classes_to_export = new String[] {"Red_Td_Tom", "Green_GFP"}


// Create the measurementExporter and start the export
def exporter  = new MeasurementExporter()
                  .imageList(imagesToExport)            // Images from which measurements will be exported
                  .separator(separator)                 // Character that separates values
                  .includeOnlyColumns(columnsToInclude) // Columns are case-sensitive
                  .exportType(exportType)               // Type of objects to export
                  .filter(obj -> obj.getPathClass() == getPathClass("Red_TdTom"))    // Keep only objects with class 'Tumor'
                  .exportMeasurements(outputFile)        // Start the export process

print "Done!"
