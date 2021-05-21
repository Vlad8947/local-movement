module local.movement.common {
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.core;

    exports local.movement.common;
    exports local.movement.common.network;
    exports local.movement.common.model;
    exports local.movement.common.view;
    exports local.movement.common.transfer;
}