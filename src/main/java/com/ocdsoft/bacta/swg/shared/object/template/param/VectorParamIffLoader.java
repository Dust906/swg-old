package com.ocdsoft.bacta.swg.shared.object.template.param;

import com.ocdsoft.bacta.swg.shared.iff.chunk.ChunkReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by crush on 8/20/2014.
 */
public class VectorParamIffLoader implements TemplateBaseIffLoader<VectorParam> {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public void load(VectorParam object, ChunkReader reader) {
        logger.trace("Loading.");
    }
}