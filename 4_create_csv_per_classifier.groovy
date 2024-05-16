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

// Create the measurementExporter and start the export
def exporter  = new MeasurementExporter()
                  .imageList(imagesToExport)            // Images from which measurements will be exported
                  .separator(separator)                 // Character that separates values
                  .includeOnlyColumns(columnsToInclude) // Columns are case-sensitive
                  .exportType(exportType)               // Type of objects to export
                  .filter(obj -> obj.getPathClass() == getPathClass("Red_TdTom"))    // Keep only objects with class 'Tumor'
                  .exportMeasurements(outputFile)        // Start the export process

print "Done!"