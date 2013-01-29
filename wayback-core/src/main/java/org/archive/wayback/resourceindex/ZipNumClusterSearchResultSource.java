package org.archive.wayback.resourceindex;

import java.io.IOException;

import org.archive.format.gzip.zipnum.ZipNumCluster;
import org.archive.format.gzip.zipnum.ZipNumParams;
import org.archive.format.gzip.zipnum.blockloader.BlockLoader;
import org.archive.util.iterator.CloseableIterator;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.exception.ResourceIndexNotAvailableException;
import org.archive.wayback.resourceindex.cdx.CDXLineToSearchResultAdapter;
import org.archive.wayback.util.AdaptedIterator;

/**
 * SearchResultSource wrapper for new implementation of CDX input source, including ZipNumCluster and CDX File
 * from archive-commons
 * @author ilya
 *
 */

public class ZipNumClusterSearchResultSource implements SearchResultSource {

	protected String clusterUri;
	
	protected String summaryFile;
	
	protected BlockLoader blockLoader;
	
	protected ZipNumCluster cluster;
	protected ZipNumParams params = null;
		
	public void init() throws IOException
	{
		this.cluster = new ZipNumCluster(clusterUri, summaryFile, blockLoader);
	}
	
	@Override
	public CloseableIterator<CaptureSearchResult> getPrefixIterator(
			String prefix) throws ResourceIndexNotAvailableException {
		
		try {
			
			String urlkey = prefix;
			
			int space = prefix.indexOf(' ');
			if (space >= 0) {
				urlkey = prefix.substring(0, space);
			}
			
			return new AdaptedIterator<String,CaptureSearchResult>
				(cluster.getCDXLineIterator(urlkey, prefix, params), new CDXLineToSearchResultAdapter());
			
		} catch (IOException e) {
			throw new ResourceIndexNotAvailableException(e.toString());
		}
	}

	@Override
	public CloseableIterator<CaptureSearchResult> getPrefixReverseIterator(
			String prefix) throws ResourceIndexNotAvailableException {
		
		throw new ResourceIndexNotAvailableException("Unsupported");
	}

	@Override
	public void cleanup(CloseableIterator<CaptureSearchResult> c)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() throws IOException {
		// TODO Auto-generated method stub

	}

	public String getClusterUri() {
		return clusterUri;
	}

	public void setClusterUri(String clusterUri) {
		this.clusterUri = clusterUri;
	}

	public String getSummaryFile() {
		return summaryFile;
	}

	public void setSummaryFile(String summaryFile) {
		this.summaryFile = summaryFile;
	}

	public BlockLoader getBlockLoader() {
		return blockLoader;
	}

	public void setBlockLoader(BlockLoader blockLoader) {
		this.blockLoader = blockLoader;
	}

	public ZipNumParams getParams() {
		return params;
	}

	public void setParams(ZipNumParams params) {
		this.params = params;
	}
}
