public class UploadFileLWC_Controller {
  
    @AuraEnabled
    public static String fetchMerge(String objectAndSoql,String mergeFieldName){
        System.debug('objectAndSoql '+objectAndSoql);
        System.debug('mergeFieldName1 '+mergeFieldName);
        Map<String,String> objectAndSoql1 = (Map<String,String>) JSON.deserialize(objectAndSoql, Map<String,String>.class);
        Map<String,List<String>> mergeFieldName1= (Map<String,List<String>>) JSON.deserialize(mergeFieldName, Map<String,List<String>>.class);
        Map<String,Object> resMap = new Map<String,Object>();
        System.debug('objectAndSoql '+objectAndSoql1);
        System.debug('mergeFieldName1 '+mergeFieldName1);
        for(String objectName:objectAndSoql1.keySet()){
            system.debug('query ' + objectAndSoql1.get(objectName));
            String query = objectAndSoql1.get(objectName);
            SOBject sobject1 = Database.query(query);
            for(String fieldNames : mergeFieldName1.get(objectName)){
                resMap.put('\'&lt;&lt;'+ objectName +'~' +  fieldNames +'&gt;&gt;\'',sobject1.get(fieldNames));
            }
        }
        System.debug('resMap '+resMap);
        return JSON.serialize(resMap);
    }
    
}