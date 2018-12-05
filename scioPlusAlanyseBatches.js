

const fs = require('fs');

const first_arg_type = process.argv[2];
const first_arg = process.argv[3];

const second_arg_type = process.argv[4];
const second_arg = process.argv[5];

var data_dir;   // to save the --data-dir argument
var output_dir; // to save the --output-dir arguement


function Batch(batchId) {
    this.batchId = batchId;
    this.slaughterWeight = {};
    this.slaughterAge = {};
    this.predictions = [];
}


function Prediction(batchId, weight, birdAge) {
    this.batchId = batchId;
    this.predictedWeight = weight;
    this.birdAge = birdAge;
}




// to ensure that the right arguments are passed, a data dir path must be used
if (first_arg_type != '--data-dir' && second_arg_type != '--data-dir') {
    console.log('usage: Node filename.js --data-dir [path to data dir] --output-dir [path to save output]');
    console.log('missing arguement --data-dir');
    return;
}
else if (first_arg_type == '--data-dir') {
    data_dir = first_arg;
}

else if (second_arg_type == '--data-dir') {
    data_dir = second_arg;
}


if (first_arg_type != '--output-dir' && second_arg_type != '--output-dir') {
    console.log('usage: Node filename.js --data-dir [path to data dir] --output-dir [path to save output]');
    console.log('missing arguement --output-dir ');
    return;
}

else if(first_arg_type == '--output-dir') {
    output_dir = first_arg;
}

else if(second_arg_type == '--output-dir') {
    output_dir = second_arg;
}



let predictions = [];   // where we save the prediction file names
let slaughters = [];    // where we save slaughter file names

let store = {};         //  object to store all transactions using batchIds as key and value as a Batch Object
let analysis_report = [];   //  anaylis report array

run();

function run() {
    console.log("checking directory for input files.......");
    fs.readdir(data_dir, (err, files) => { // read input file names from the data dir
        files.forEach(file => {
            if (file.includes('prediction')) {
                predictions.push(file);
            }
            else if (file.includes('slaughter')) {
                slaughters.push(file);
            }
        });
        
        processPredictionFiles();
        processSlaughterFiles();
        generateReport();
    })     
}


function processPredictionFiles() {
    console.log('processing predictions......');

    predictions.forEach(filename => {
        var str = fs.readFileSync(data_dir + '/' + filename).toString('utf-8');
        let prediction = JSON.parse(str);

        var batch = new Batch(prediction.batchId);
        var p = new Prediction(prediction.batchId, prediction.predictedWeight, prediction.birdAge);
        batch.predictions.push(p);

        store[prediction.batchId] ? store[prediction.batchId].predictions.push(p) : store[prediction.batchId] = batch;     

    });
}


function processSlaughterFiles() {
    console.log('processing slaughter files......');

    slaughters.forEach(filename => {
        var str = fs.readFileSync(data_dir + '/' + filename).toString('utf-8');
        let slaughter = JSON.parse(str);

        store[slaughter.batchId].slaughterWeight = slaughter.slaughterWeight;
        store[slaughter.batchId].slaughterAge = slaughter.slaughterAge;
    });
}

function generateReport() {
    console.log('generating report......');

    for (let key in store) {

        if (store[key].slaughterAge > 0) {
            let batch = store[key];
            let sumOfDifference = 0;
            let lowestDiff = 10000000;
            var best = {};

            for (let pred of batch.predictions) {

                let diff = Math.abs(batch.slaughterWeight - pred.predictedWeight);
                sumOfDifference += diff;

                if (diff < lowestDiff) {
                    lowestDiff = diff;
                    best.predictedWeight = pred.predictedWeight;
                    best.birdAge = pred.birdAge;
                }         

            }

            let out = {};
            out.batchId = key;
            out.averageDifference = Math.round(sumOfDifference / batch.predictions.length);
            out.bestPrediction = best;
            analysis_report.push(out);

        }
    }

    console.log(JSON.stringify(analysis_report));

    fs.writeFile(output_dir,JSON.stringify(analysis_report), (err) => {
        if (err) console.log('an error occured writing output file');
        console.log('output file has been generated!');
    });


}

