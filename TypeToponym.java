/*
 * Class TypeToponym
 *
 */

/**
 *
 * @Justine Mugisa
 */
public class TypeToponym implements IGetId {
   private String name;
   private String category;
   private String code;

   public TypeToponym(String name, String category, String code) {
       this.name = name;
       this.category = category;
       this.code = code;
   }

   public String getCategory() {
       return category;
   }

   public void setCategory(String category) {
       this.category = category;
   }

   public String getCode() {
       return code;
   }

   public void setCode(String code) {
       this.code = code;
   }

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }

   public String getId() {
     return this.code;
   }
}