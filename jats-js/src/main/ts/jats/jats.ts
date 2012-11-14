///<reference path='../lib/underscore.d.ts' />
declare var _ :underscore;

class JATS {

    public static OBJECT_MAP = {}

    public static fromJson(obj, objectMap: {}) {

        if (typeof objectMap === "undefined") {
            objectMap = OBJECT_MAP;
        }

        if (typeof obj === "undefined") {
            return;
        }
        else if (obj == null) {
            return null;
        }
        else {
            var jatsType = obj["jatsType"];
            if (typeof jatsType !== "undefined") {
                obj = _.extend(new jatsType(), obj);
            }

            var id = obj["id"];
            if (typeof id !== "undefined") {
                //obj has id property
                objectMap[id] = obj;
            }

            var ref = obj["$ref"];
            if (typeof ref !== "undefined") {
                //obj has #ref property
                //locate referenced id from object Map and return it
                return objectMap[ref];
            }

            for (var prop in obj) {
                if (typeof obj[prop] === "object") {
                    obj[prop] = fromJson(obj[prop], objectMap);
                }
            }
            return obj;
        }
    }

}
