(set-env!
  :source-paths #{"src/clj" "src/cljs"}
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  [javax.xml.bind/jaxb-api "2.3.0" :scope "test"] ; necessary for Java 9 compatibility
                  ; project deps
                  [org.clojure/clojure "1.9.0"]
                  [org.clojure/clojurescript "1.9.946" :scope "test"]
                  [reagent "0.7.0" :scope "test"]
                  [ring "1.6.3"]])

(task-options!
  pom {:project '{{app-name}}
       :version "1.0.0-SNAPSHOT"
       :description "FIXME: write description"}
  aot {:namespace #{'{{namespace}}}}
  jar {:main '{{namespace}}})

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '{{namespace}})

(deftask run []
  (comp
    (with-pass-thru _
      ({{namespace}}/dev-main))
    (watch)
    (reload :asset-path "public")
    (cljs
      :source-map true
      :optimizations :none
      :compiler-options {:asset-path "main.out"})
    (target)))

(deftask build []
  (comp
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)
    (sift :include #{#"\.jar$"})
    (target)))

