import {
    LightningElement,
    api
} from 'lwc';
import {
    deleteRecord
} from 'lightning/uiRecordApi';
import {
    ShowToastEvent
} from 'lightning/platformShowToastEvent';
//import insertVersion from '@salesforce/apex/UploadFileLWC_Controller.insertVersion';
import fetchMerge from '@salesforce/apex/UploadFileLWC_Controller.fetchMerge';


export default class UploadFileLWC extends LightningElement {

    showSpinner;
    @api recordId;
    @api xmlString;
    @api xmlName;
    @api downloadUrl;
    @api documentId;
    token = 'a629e6b07e43542d95800ad6187b5808';
    get acceptedFormats() {
        return ['.doc', '.docx'];
    }

    

   
    handleUploadFinished(event) {
        this.showSpinner = true;
        // Get the list of uploaded files
        const uploadedFiles = event.detail.files;
        var file = uploadedFiles[0];
        console.log('file ', file);

        

        var formdata = new FormData();
        formdata.append("docfile", file, file.name);
        formdata.append("token", this.token);

        var requestOptions = {
            method: 'POST',
            body: formdata,
            redirect: 'follow'
        };

        fetch("https://houseupapi.com:8443/pdfsolutions_apis/api/covert/doxtoxml", requestOptions)
            .then(response => response.text())
            .then(result => {
                console.log('1st callout result', result);
                var res = JSON.parse(result);
                if (res.responseCode == '200') {
                    var xmlLink = res.results;
                    console.log('xmlLink ',xmlLink);
                    var xmlrequestOptions = {
                        method: 'GET',
                        redirect: 'follow'
                      };
                      
                      fetch("https://houseupapi.com:8443/pdfsolutions_apis/api/gettext?url="+xmlLink, xmlrequestOptions)
                        .then(xmlresponse => xmlresponse.text())
                        .then(xmlresult => {
                            console.log(xmlresult);
                            this.xmlString = xmlresult;
                            this.regex(file.name.split(".")[0]);

                           /*  var xmlres = JSON.parse(xmlresult);
                            if (xmlres.responseCode == '200') {
                            } */
                            })
                        .catch(xmlerror => console.log(' xmlerror', xmlerror));
                }
            })
            .catch(error => {
                console.log('error', error);
                this.showSpinner = false;
            });


    }

    regex(name) {
        //this.xmlString = atob(res);
        //console.log(this.xmlString);
        //var regex = /&lt;&lt;[A-Za-z]*_*c*\.([a-zA-Z0-9]{15}|[a-zA-Z0-9]{18})\.[a-zA-Z]*_*c&gt;&gt;/gm; // /&lt;&lt;[A-Za-z]*&gt;&gt;/gm;
        var regex = /&lt;&lt;[a-zA-Z0-9_]*_*c*\~*[a-zA-Z0-9_]*_*c*&gt;&gt;/gm;

        var regexArray = this.xmlString.match(regex);
        var objectAndSoql = new Map();
        var fieldApiNames = new Map();
        if (regexArray) {
            //null check
            regexArray.forEach((element) => {
                console.log(element, ' ');
                var resultantString = element.replace("&lt;&lt;", "").replace("&gt;&gt;", "");
                var objectList = resultantString.split('~');
                console.log('objectList ', objectList);
                var soql = !objectAndSoql.has(objectList[0]) ? 'SELECT Id ' + ',' + objectList[1] :
                    (objectAndSoql.get(objectList[0]).split('FROM')[0] + ',' + objectList[1]);
                soql = soql.concat(' FROM ' + objectList[0] + ' LIMIT 1');
                objectAndSoql.set(objectList[0], soql);
                var fieldsName = fieldApiNames.has(objectList[0]) ?
                    fieldApiNames.get(objectList[0]) : [];
                fieldsName.push(objectList[1]);


                fieldApiNames.set(objectList[0], fieldsName);
                

            });

            console.log('fieldApiNames ', JSON.stringify(Object.fromEntries(fieldApiNames)));
            console.log('objectAndSoql ', JSON.stringify(Object.fromEntries(objectAndSoql)));

            fetchMerge({
                    objectAndSoql: JSON.stringify(Object.fromEntries(objectAndSoql)),
                    mergeFieldName: JSON.stringify(Object.fromEntries(fieldApiNames))
                })
                .then(apexResult => {
                    var result = JSON.parse(apexResult);
                    debugger;
                    regexArray.forEach((element) => {
                        console.log(element + " = " + result['\'' + element + '\'']);
                        var data = result['\'' + element + '\''] ? result['\'' + element + '\''] : '';
                        this.xmlString = this.xmlString.replace(element, data);

                    });

                    this.xmlName = name + ".xml";
                    console.log("this.xmlString updated ", this.xmlString);

                    this.getPdfFileLink();


                }).catch(error => {
                    this.showSpinner = false;

                });
        }else {
            this.showSpinner = false;
            alert("No Merge Tags Found..Please either add the merge tags or check the syntax of merge tags");
        }
    }
    download(url, filename) {
        const link = document.createElement("a");
        link.href = url;
        link.download = filename;
        link.click();
        this.showSpinner = false;

    }

    downloadPdf(event) {
        console.log('event.detail.performCallout ', event.detail.performCallout);
        if (event.detail.performCallout) {
            this.getPdfFileLink();
        }
        console.log('documentId ', this.documentId);
        deleteRecord(this.documentId)
            .then(() => {

            }).catch(error => {
                console.log('er ', error.body.message);
            })
    }
    getPdfFileLink() {
        var name = this.xmlName.split(".")[0];
        this.showSpinner = true;

        var xmlFile = new File([this.xmlString], this.xmlName, {
            type: "text/xml",
            lastModified: new Date().getTime()
        })
        var formdata = new FormData();
        formdata.append("xmlFile", xmlFile, this.xmlName);
        formdata.append("token", this.token);
        formdata.append('Access-Control-Allow-Origin', '*');
        //a629e6b07e43542d95800ad6187b5808
        var requestOptions = {
            method: 'POST',
            body: formdata,
            redirect: 'follow'
        };

        fetch("https://houseupapi.com:8443/pdfsolutions_apis/api/covert/xmltopdf", requestOptions)
            .then(response => response.text())
            .then(result => {
                console.log('2nd callout ', result);
                console.log('2nd callout ', JSON.stringigfy(result.getBody()));


                var callOutRes = JSON.parse(result);

                if(callOutRes.responseCode == '200'){
                    var link = callOutRes.results;
                    console.log('link ', link);
                    this.download(link, name + '.pdf');
                }

            })
            .catch(error => {
                console.log('error', error);
                this.showSpinner = false;

            });
    }
}